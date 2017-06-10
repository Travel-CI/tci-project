package com.travelci.docker.runner.services;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerCertificates;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Container;
import com.spotify.docker.client.messages.ContainerChange;
import com.spotify.docker.client.messages.Image;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static com.spotify.docker.client.DockerClient.ListContainersParam.allContainers;
import static com.spotify.docker.client.DockerClient.ListImagesParam.allImages;
import static org.assertj.core.api.Assertions.assertThat;

public class DockerServiceIT {

    private DockerRunnerService dockerRunnerService;

    private DefaultDockerClient dockerClient;

    @Before
    public void setUp() throws DockerCertificateException {

        final String windowsSocketUri = "https://192.168.99.100:2376";
        final String windowsCertificatesUri = "C:\\Users\\Julien\\.docker\\machine\\certs";
        final String unixSocketUri = "unix:///var/run/docker.sock";

        if (System.getProperty("os.name").contains("Windows"))
            dockerClient = DefaultDockerClient.builder()
                .uri(windowsSocketUri)
                .dockerCertificates(new DockerCertificates(Paths.get(windowsCertificatesUri)))
                .build();
        else
            dockerClient = new DefaultDockerClient(unixSocketUri);

        dockerRunnerService = new DockerRunnerServiceImpl(dockerClient,
            "", "");
    }

    @Test
    @Ignore
    public void shouldBuildDockerImageAndDeleteIt() throws IOException, DockerException, InterruptedException {

        final String imageName = "test-tci-image";
        final String replaceInImageName = "sha256:";

        final String dockerfileFolder = getClass().getClassLoader()
            .getResource("Dockerfile").toString()
            .replace("file:", "")
            .replace("Dockerfile", "");

        final String createdImageId = dockerRunnerService
            .buildImageFromDockerFile(imageName, dockerfileFolder);

        assertThat(createdImageId).isNotNull();
        assertThat(createdImageId).isNotEmpty();

        final Optional<Image> createdImage = dockerClient.listImages(allImages())
            .stream()
            .filter(image -> image.id().replaceFirst(replaceInImageName, "").startsWith(createdImageId))
            .findFirst();

        assertThat(createdImage.isPresent()).isTrue();
        assertThat(createdImage.get().repoTags()).hasSize(1);
        assertThat(createdImage.get().repoTags().get(0)).isEqualTo(imageName + ":latest");
        assertThat(createdImage.get().size()).isNotZero();

        dockerClient.removeImage(createdImageId, true, false);

        final Optional<Image> checkRemovedImage = dockerClient.listImages(allImages())
            .stream()
            .filter(image -> image.id().replaceFirst(replaceInImageName, "").startsWith(createdImageId))
            .findFirst();

        assertThat(checkRemovedImage.isPresent()).isFalse();
    }

    @Test
    @Ignore
    public void shouldCreateASimpleBusyBoxContainerAndRunAndStopAndDeleteIt() throws DockerException, InterruptedException {

        final String imageName = "busybox:1";

        dockerClient.pull(imageName);

        final String containerId = dockerRunnerService.startContainer(imageName);
        assertThat(containerId).isNotNull();
        assertThat(containerId).isNotEmpty();

        // Check if container is created and started
        final Optional<Container> startedContainer = dockerClient.listContainers(allContainers())
            .stream()
            .filter(container -> containerId.equals(container.id()))
            .findFirst();
        assertThat(startedContainer.isPresent()).isTrue();
        assertThat(startedContainer.get().image()).isEqualTo(imageName);
        assertThat(startedContainer.get().status()).contains("Up");

        // Stop Container and Delete It
        dockerRunnerService.stopContainer(containerId);
        final Optional<Container> checkDeletedContainer = dockerClient.listContainers(allContainers())
            .stream()
            .filter(container -> containerId.equals(container.id()))
            .findFirst();
        assertThat(checkDeletedContainer.isPresent()).isFalse();
    }

    @Test
    @Ignore
    public void shouldCreateBusyBoxContainerAndCopyFilesInContainerAndDeleteIt() throws DockerException, InterruptedException {

        final String projectsRootFolder = getClass().getClassLoader()
            .getResource("Dockerfile").toString()
            .replace("file:", "")
            .replace("Dockerfile", "");
        final String projectFolderInContainer = "/";
        final String imageName = "busybox:1";

        // Create Service with projectsRootFolder = docker-runner test resources folder
        dockerRunnerService = new DockerRunnerServiceImpl(dockerClient,
            projectsRootFolder, projectFolderInContainer);
        dockerClient.pull(imageName);

        // Start Container and Copy test resources files (Dockerfile && application.yml)
        final String containerId = dockerRunnerService.startContainer(imageName);
        assertThat(containerId).isNotNull();
        assertThat(containerId).isNotEmpty();

        // Check container changes (create/edit/delete file is a change)
        final List<ContainerChange> containerChanges = dockerClient.inspectContainerChanges(containerId);
        assertThat(containerChanges).isNotNull();
        assertThat(containerChanges.isEmpty()).isFalse();
        assertThat(containerChanges.size()).isEqualTo(2);
        assertThat(containerChanges.get(0).path()).isEqualTo("/Dockerfile");
        assertThat(containerChanges.get(1).path()).isEqualTo("/application.yml");

        // Stop Container and Delete It
        dockerRunnerService.stopContainer(containerId);
        final Optional<Container> checkDeletedContainer = dockerClient.listContainers(allContainers())
            .stream()
            .filter(container -> containerId.equals(container.id()))
            .findFirst();
        assertThat(checkDeletedContainer.isPresent()).isFalse();
    }
}
