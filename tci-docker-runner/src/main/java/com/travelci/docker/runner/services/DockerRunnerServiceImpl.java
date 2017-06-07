package com.travelci.docker.runner.services;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.travelci.docker.runner.entities.CommandDto;
import com.travelci.docker.runner.exceptions.DockerBuildImageException;
import com.travelci.docker.runner.exceptions.DockerStartContainerException;
import com.travelci.docker.runner.exceptions.DockerStopContainerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

// https://github.com/spotify/docker-client
@Service
public class DockerRunnerServiceImpl implements DockerRunnerService {

    private final DockerClient docker;

    private final String projectsRootFolder;
    private final String projectFolderInContainer;

    public DockerRunnerServiceImpl(final DockerClient docker,
                                   @Value("info.docker.projectsRootFolderInHost")
                                   final String projectsRootFolder,
                                   @Value("info.docker.projectFolderInContainer")
                                   final String projectFolderInContainer) {
        this.docker = docker;
        this.projectsRootFolder = projectsRootFolder;
        this.projectFolderInContainer = projectFolderInContainer;
    }

    @Override
    public String buildImageFromDockerFile(final String imageName, final String dockerfileLocation) {

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
    public String startContainer(final String imageId) {

        /*final Bind volume = Bind.builder()
            .from(projectsRootFolder)
            .to(projectFolderInContainer)
            .build();*/

        final ContainerConfig containerConfig = ContainerConfig.builder()
            .image(imageId)
            //.hostConfig(HostConfig.builder().appendBinds(volume).build())
            .build();

        try {
            final ContainerCreation container = docker.createContainer(containerConfig);
            docker.startContainer(container.id());

            if (projectsRootFolder != null && !projectsRootFolder.isEmpty()
            && projectFolderInContainer != null && !projectFolderInContainer.isEmpty())
                docker.copyToContainer(Paths.get(projectsRootFolder), container.id(), projectFolderInContainer);

            return container.id();
        } catch (DockerException | InterruptedException | IOException e) {
            throw new DockerStartContainerException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void executeCommandsInContainer(final List<CommandDto> commands) {

        try {
            docker.pull("busybox:1");
        } catch (DockerException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopContainer(final String containerId) {

        try {
            docker.stopContainer(containerId, 5);
            docker.removeContainer(containerId);
        } catch (DockerException | InterruptedException e) {
            throw new DockerStopContainerException(e.getMessage(), e.getCause());
        }
    }
}
