package com.travelci.docker.runner.services;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Container;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static com.spotify.docker.client.DockerClient.ListContainersParam.allContainers;
import static org.assertj.core.api.Assertions.assertThat;

public class DockerServiceIT {

    private DockerRunnerService dockerRunnerService;

    private DefaultDockerClient dockerClient;

    private static final String DOCKER_SOCKET = System.getProperty("os.name").contains("Windows")
                                                    ? "http://localhost:2375"
                                                    : "unix:///var/run/docker.sock";

    @Before
    public void setUp() {

        dockerClient = new DefaultDockerClient(DOCKER_SOCKET);

        dockerRunnerService = new DockerRunnerServiceImpl(dockerClient,
            "", "");
    }

    @Test
    public void shouldCreateASimpleBusyBoxContainerAndRunAndStopAndDelete() throws DockerException, InterruptedException {

        final String imageName = "busybox:1";

        dockerClient.pull(imageName);

        final String containerId = dockerRunnerService.startContainer(imageName);

        assertThat(containerId).isNotNull();
        assertThat(containerId).isNotEmpty();

        final Optional<Container> startedContainer = dockerClient.listContainers(allContainers())
            .stream()
            .filter(container -> containerId.equals(container.id()))
            .findFirst();

        assertThat(startedContainer.isPresent()).isTrue();
        assertThat(startedContainer.get().image()).isEqualTo(imageName);
        assertThat(startedContainer.get().status()).contains("Exited");

        dockerRunnerService.stopContainer(containerId);

        final Optional<Container> checkDeletedContainer = dockerClient.listContainers(allContainers())
            .stream()
            .filter(container -> containerId.equals(container.id()))
            .findFirst();

        assertThat(checkDeletedContainer.isPresent()).isFalse();
    }
}
