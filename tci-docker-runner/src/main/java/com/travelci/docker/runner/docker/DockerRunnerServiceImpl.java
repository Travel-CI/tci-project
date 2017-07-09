package com.travelci.docker.runner.docker;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ExecCreation;
import com.travelci.docker.runner.command.entities.CommandDto;
import com.travelci.docker.runner.command.entities.DockerCommandsProject;
import com.travelci.docker.runner.docker.exceptions.*;
import com.travelci.docker.runner.logger.LoggerService;
import com.travelci.docker.runner.logger.entities.BuildDto;
import com.travelci.docker.runner.logger.entities.StepDto;
import com.travelci.docker.runner.project.entities.ProjectDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.spotify.docker.client.DockerClient.ExecCreateParam.attachStderr;
import static com.spotify.docker.client.DockerClient.ExecCreateParam.attachStdout;

@Service
@Slf4j
@RefreshScope
class DockerRunnerServiceImpl implements DockerRunnerService {

    private final LoggerService loggerService;
    private final DockerClient docker;

    private final String projectFolderInContainer;

    DockerRunnerServiceImpl(final LoggerService loggerService,
                            final DockerClient docker,
                            @Value("${info.docker.projectFolderInContainer}")
                            final String projectFolderInContainer) {
        this.loggerService = loggerService;
        this.docker = docker;
        this.projectFolderInContainer = projectFolderInContainer;
    }

    @Override
    public void startDockerRunnerEngine(final DockerCommandsProject dockerCommandsProject) {

        final ProjectDto project = dockerCommandsProject.getProject();
        final List<CommandDto> commands = dockerCommandsProject.getCommands();

        final String projectLocation = project.getName();

        final String customDockerfileLocation = project.getDockerfileLocation() != null
                && !project.getDockerfileLocation().isEmpty()
            ? project.getDockerfileLocation()
            : "";

        final String dockerFileFolder = new StringBuilder()
            .append(projectLocation)
            .append(customDockerfileLocation)
            .toString();

        // Name in LowerCase : Docker Build Convention
        final String imageName = projectLocation.substring(projectLocation.lastIndexOf("/") + 1).toLowerCase();

        try {
            final String imageId = buildImageFromDockerFile(imageName, dockerFileFolder);
            final String containerId = startContainer(imageId, projectLocation);
            executeCommandsInContainer(containerId, commands, project.getCurrentBuild());
            stopContainer(containerId);
        } catch (DockerRunnerException e) {
            log.error("Error while executing Docker Engine", e);
            loggerService.endBuildByError(project.getCurrentBuild(), e.getLocalizedMessage());
            throw e;
        }
    }

    @Override
    public String buildImageFromDockerFile(final String imageName,
                                           final String dockerfileLocation) {

        try {
            final String createdImageId = docker.build(Paths.get(dockerfileLocation), imageName, message -> {
                if (message.error() != null)
                    throw new DockerException(message.error());
            });

            if (createdImageId != null)
                return createdImageId;
            else
                throw new DockerException("Error in Docker Image for the dockerfile " + dockerfileLocation);

        } catch (DockerException | InterruptedException | IOException e) {
            throw new DockerBuildImageException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public String startContainer(final String imageId, final String projectFolder) {

        final ContainerConfig containerConfig = ContainerConfig.builder()
            .image(imageId)
            .tty(true)
            .build();

        try {
            final ContainerCreation container = docker.createContainer(containerConfig);
            docker.startContainer(container.id());

            if (!projectFolder.isEmpty() && projectFolderInContainer != null
                && !projectFolderInContainer.isEmpty())
                docker.copyToContainer(Paths.get(projectFolder), container.id(), projectFolderInContainer);

            return container.id();
        } catch (DockerException | InterruptedException | IOException e) {
            throw new DockerStartContainerException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Map<String, String> executeCommandsInContainer(final String containerId,
                                                          final List<CommandDto> commands,
                                                          final BuildDto currentBuild) {

        final Map<String, String> commandResults = new LinkedHashMap<>();

        for (final CommandDto command : commands) {

            final StepDto step = loggerService.startNewStep(command, currentBuild);

            try {

                final String[] executedCommand = new String[] {
                    "sh", "-c", command.getCommand() + " 2>&1 1>~/dockerOutput | tee -a ~/dockerOutput"
                };

                final String[] stdoutStderrCommand = new String[] {
                    "sh", "-c", "cat ~/dockerOutput"
                };

                // Execute command, return stderr (if stderr string is not empty, there is a mistake with the command)
                final ExecCreation commandCreation = docker.execCreate(
                    containerId, executedCommand,
                    attachStdout(), attachStderr());

                final LogStream commandLs = docker.execStart(commandCreation.id());
                final String stderrOutput = commandLs.readFully();

                // Execute command to get the stdout / stderr of previous command
                final ExecCreation stdoutStderrCommandCreation = docker.execCreate(
                    containerId, stdoutStderrCommand,
                    attachStdout(), attachStderr());

                final LogStream stdoutStderrLs = docker.execStart(stdoutStderrCommandCreation.id());
                final String stdoutStderOutput = stdoutStderrLs.readFully();

                if (!stderrOutput.isEmpty()) {
                    log.error("Command " + command.getCommand() + " has failed");
                    loggerService.endStepByError(step, stdoutStderOutput);
                    loggerService.endBuildByError(currentBuild, "step failed : " + command.getCommand());
                    return commandResults;
                }
                else
                    loggerService.endStepBySuccess(step, stdoutStderOutput);

                commandResults.put(command.getCommand(), stdoutStderOutput);
            } catch (DockerException | InterruptedException e) {
                loggerService.endStepByError(step, e.getLocalizedMessage());
                throw new DockerExecuteCommandException(e.getMessage(), e.getCause());
            }
        }

        loggerService.endBuildBySuccess(currentBuild);

        return commandResults;
    }

    @Override
    public void stopContainer(final String containerId) {

        try {
            docker.stopContainer(containerId, 2);
            docker.removeContainer(containerId);
        } catch (DockerException | InterruptedException e) {
            throw new DockerStopContainerException(e.getMessage(), e.getCause());
        }
    }
}