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
import com.travelci.docker.runner.notifications.NotificationsService;
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
    private final NotificationsService notificationsService;
    private final DockerClient docker;

    private final String projectFolderInContainer;

    DockerRunnerServiceImpl(final LoggerService loggerService,
                            final NotificationsService notificationsService,
                            final DockerClient docker,
                            @Value("${info.docker.projectFolderInContainer}")
                            final String projectFolderInContainer) {
        this.loggerService = loggerService;
        this.notificationsService = notificationsService;
        this.docker = docker;
        this.projectFolderInContainer = projectFolderInContainer;
    }

    @Override
    public void startDockerRunnerEngine(final DockerCommandsProject dockerCommandsProject) {

        final ProjectDto project = dockerCommandsProject.getProject();
        final List<CommandDto> commands = dockerCommandsProject.getCommands();

        // Get project Folder Location and Dockerfile Location
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
            executeCommandsInContainer(containerId, commands, project);
            stopContainer(containerId);
        } catch (final DockerRunnerException e) {
            log.error("Error while executing Docker Engine", e);
            project.setCurrentBuild(
                    loggerService.endBuildByError(project.getCurrentBuild(), e.getMessage()));
            notificationsService.sendErrorNotification(project);
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

        } catch (final DockerException | InterruptedException | IOException e) {
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
        } catch (final DockerException | InterruptedException | IOException e) {
            throw new DockerStartContainerException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Map<String, String> executeCommandsInContainer(final String containerId,
                                                          final List<CommandDto> commands,
                                                          final ProjectDto project) {

        final Map<String, String> commandResults = new LinkedHashMap<>();
        final BuildDto currentBuild = project.getCurrentBuild();
        final String[] returnCodeCommand = new String[] {
            "sh", "-c", "cat status"
        };

        for (final CommandDto command : commands) {

            final StepDto step = loggerService.startNewStep(command, currentBuild);

            try {

                final String[] realCommand = new String[] {
                    "sh", "-c", "echo 0 > status; " + command.getCommand() + " || echo $? > status"
                };

                // Execute command, return stdout and stderr
                String stdoutStderrOutput = executeCommandInContainer(containerId, realCommand);

                // Execute command to get the status code of the previous command
                final String returnCodeOutput = executeCommandInContainer(containerId, returnCodeCommand);

                final String formattedReturnCode = returnCodeOutput.replace("\n", "").replace("\r", "");
                final Boolean commandSuccess = "0".equals(formattedReturnCode);

                if (!command.getEnableLogs())
                    stdoutStderrOutput = (commandSuccess) ? "Command successfully executed." : "Command failed.";

                if (!commandSuccess) {
                    log.error("Command " + command.getCommand() + " failed for build "
                        + currentBuild.getId() + " in project " + currentBuild.getProjectId());

                    loggerService.endStepByError(step, stdoutStderrOutput);
                    project.setCurrentBuild(
                        loggerService.endBuildByError(currentBuild, "step failed : " + command.getCommand())
                    );
                    notificationsService.sendErrorNotification(project);

                    return commandResults;
                }
                else
                    loggerService.endStepBySuccess(step, stdoutStderrOutput);

                commandResults.put(command.getCommand(), stdoutStderrOutput);
            } catch (final DockerException | InterruptedException e) {
                loggerService.endStepByError(step, e.getMessage());
                throw new DockerExecuteCommandException(e.getMessage(), e.getCause());
            }
        }

        project.setCurrentBuild(loggerService.endBuildBySuccess(currentBuild));
        notificationsService.sendSuccessNotification(project);

        return commandResults;
    }

    @Override
    public void stopContainer(final String containerId) {

        try {
            docker.stopContainer(containerId, 2);
            docker.removeContainer(containerId);
        } catch (final DockerException | InterruptedException e) {
            throw new DockerStopContainerException(e.getMessage(), e.getCause());
        }
    }

    private String executeCommandInContainer(final String containerId, final String[] command)
        throws DockerException, InterruptedException {

        final ExecCreation commandCreation = docker.execCreate(
            containerId, command,
            attachStdout(), attachStderr());

        final LogStream commandLs = docker.execStart(commandCreation.id());
        return commandLs.readFully();
    }
}
