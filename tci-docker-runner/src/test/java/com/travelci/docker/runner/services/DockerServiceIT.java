package com.travelci.docker.runner.services;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerCertificates;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Container;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.Optional;

import static com.spotify.docker.client.DockerClient.ListContainersParam.allContainers;
import static org.assertj.core.api.Assertions.assertThat;

public class DockerServiceIT {

    private DockerRunnerService dockerRunnerService;

    private DefaultDockerClient dockerClient;

    private static final String DOCKER_SOCKET = System.getProperty("os.name").contains("Windows")
                                                    ? "https://192.168.99.100:2376"
                                                    : "unix:///var/run/docker.sock";

    @Before
    public void setUp() throws DockerCertificateException {

        if (System.getProperty("os.name").contains("Windows"))
            dockerClient = DefaultDockerClient.builder()
                .uri(DOCKER_SOCKET)
                .dockerCertificates(new DockerCertificates(Paths.get("C:\\Users\\Julien\\.docker\\machine\\certs")))
                .build();
        else
            dockerClient = new DefaultDockerClient(DOCKER_SOCKET);

        dockerRunnerService = new DockerRunnerServiceImpl(dockerClient,
            "", "");
    }

    @Test
    @Ignore
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
        //assertThat(startedContainer.get().status()).contains("Exited");

        dockerRunnerService.stopContainer(containerId);

        final Optional<Container> checkDeletedContainer = dockerClient.listContainers(allContainers())
            .stream()
            .filter(container -> containerId.equals(container.id()))
            .findFirst();

        assertThat(checkDeletedContainer.isPresent()).isFalse();
    }
}
