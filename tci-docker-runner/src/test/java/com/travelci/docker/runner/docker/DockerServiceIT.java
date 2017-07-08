package com.travelci.docker.runner.docker;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Container;
import com.spotify.docker.client.messages.ContainerChange;
import com.spotify.docker.client.messages.Image;
import com.travelci.docker.runner.command.entities.CommandDto;
import com.travelci.docker.runner.logger.LoggerService;
import com.travelci.docker.runner.logger.entities.BuildDto;
import com.travelci.docker.runner.logger.entities.StepDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.spotify.docker.client.DockerClient.ListContainersParam.allContainers;
import static com.spotify.docker.client.DockerClient.ListImagesParam.allImages;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DockerServiceIT {

    private DockerRunnerService dockerRunnerService;
    private DefaultDockerClient dockerClient;
    @Mock private LoggerService loggerService;

    private static final String BUSYBOX_TEST_IMAGE = "busybox:1";

    @Before
    public void setUp() throws DockerCertificateException {

        final String dockerSocket = "http://localhost:2375";

        dockerClient = new DefaultDockerClient(dockerSocket);
        dockerRunnerService = new DockerRunnerServiceImpl(loggerService, dockerClient, "");
    }

    @Test
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
    public void shouldCreateBusyBoxContainerAndRunAndStopAndDeleteIt() throws DockerException, InterruptedException {

        dockerClient.pull(BUSYBOX_TEST_IMAGE);

        final String containerId = dockerRunnerService.startContainer(BUSYBOX_TEST_IMAGE, "");
        assertThat(containerId).isNotNull();
        assertThat(containerId).isNotEmpty();

        // Check if container is created and started
        final Optional<Container> startedContainer = dockerClient.listContainers(allContainers())
            .stream()
            .filter(container -> containerId.equals(container.id()))
            .findFirst();
        assertThat(startedContainer.isPresent()).isTrue();
        assertThat(startedContainer.get().image()).isEqualTo(BUSYBOX_TEST_IMAGE);
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
    public void shouldCreateBusyBoxContainerAndStartAndCopyFilesInContainerAndDeleteIt() throws DockerException, InterruptedException {

        final String projectFolder = getClass().getClassLoader()
            .getResource("Dockerfile").toString()
            .replace("file:", "")
            .replace("Dockerfile", "");
        final String projectFolderInContainer = "/";

        // Create Service with projectsRootFolder = docker-runner test resources folder
        dockerRunnerService = new DockerRunnerServiceImpl(loggerService, dockerClient, projectFolderInContainer);
        dockerClient.pull(BUSYBOX_TEST_IMAGE);

        // Start Container and Copy test resources files (Dockerfile && application.yml)
        final String containerId = dockerRunnerService.startContainer(BUSYBOX_TEST_IMAGE, projectFolder);
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

    @Test
    public void shouldExecutePwdCommandInBusyBoxContainer() throws DockerException, InterruptedException {

        when(loggerService.startNewStep(any(CommandDto.class), any(BuildDto.class)))
            .thenReturn(StepDto.builder().build());

        dockerClient.pull(BUSYBOX_TEST_IMAGE);

        final String containerId = dockerRunnerService.startContainer(BUSYBOX_TEST_IMAGE, "");
        assertThat(containerId).isNotNull();
        assertThat(containerId).isNotEmpty();

        final List<CommandDto> commandList = new ArrayList<>();
        commandList.add(CommandDto.builder().command("pwd").build());

        final Map<String, String> results = dockerRunnerService.executeCommandsInContainer(containerId, commandList, null);
        assertThat(results.isEmpty()).isFalse();
        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get("pwd")).isEqualTo("/\n");

        // Stop Container and Delete It
        dockerRunnerService.stopContainer(containerId);
        final Optional<Container> checkDeletedContainer = dockerClient.listContainers(allContainers())
            .stream()
            .filter(container -> containerId.equals(container.id()))
            .findFirst();
        assertThat(checkDeletedContainer.isPresent()).isFalse();
    }
}
