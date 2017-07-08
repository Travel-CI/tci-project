package com.travelci.docker.runner.docker;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ExecCreation;
import com.travelci.docker.runner.input.entities.CommandDto;
import com.travelci.docker.runner.input.entities.DockerCommandsProject;
import com.travelci.docker.runner.input.entities.ProjectDto;
import com.travelci.docker.runner.docker.exceptions.DockerBuildImageException;
import com.travelci.docker.runner.docker.exceptions.DockerExecuteCommandException;
import com.travelci.docker.runner.docker.exceptions.DockerStartContainerException;
import com.travelci.docker.runner.docker.exceptions.DockerStopContainerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.spotify.docker.client.DockerClient.ExecCreateParam.attachStderr;
import static com.spotify.docker.client.DockerClient.ExecCreateParam.attachStdout;

@Service
class DockerRunnerServiceImpl implements DockerRunnerService {

    private final DockerClient docker;

    private final String projectFolderInContainer;

    DockerRunnerServiceImpl(final DockerClient docker,
                            @Value("${info.docker.projectFolderInContainer}")
                            final String projectFolderInContainer) {
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

        final String imageId = buildImageFromDockerFile(imageName, dockerFileFolder);
        final String containerId = startContainer(imageId, projectLocation);
        executeCommandsInContainer(containerId, commands);
        stopContainer(containerId);
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
                                                          final List<CommandDto> commands) {

        final Map<String, String> commandResults = new LinkedHashMap<>();

        for (final CommandDto command : commands) {

            try {
                final ExecCreation execCreation = docker.execCreate(
                    containerId, command.getCommand().split(" "),
                    attachStdout(), attachStderr());

                final LogStream output = docker.execStart(execCreation.id());
                final String execOutput = output.readFully();

                commandResults.put(command.getCommand(), execOutput);

                // TODO Get errors log

                System.out.println(command.getCommand() + " " + execOutput);
                // TODO Log command result here

            } catch (DockerException | InterruptedException e) {
                throw new DockerExecuteCommandException(e.getMessage(), e.getCause());
            }
        }

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
