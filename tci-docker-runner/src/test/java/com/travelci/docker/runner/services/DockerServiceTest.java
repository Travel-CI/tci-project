package com.travelci.docker.runner.services;

import com.spotify.docker.client.DefaultDockerClient;
import org.junit.Before;
import org.junit.Test;

public class DockerServiceTest {

    private DockerRunnerService dockerRunnerService;

    @Before
    public void setUp() {
        final DefaultDockerClient docker = new DefaultDockerClient("");
    }

    @Test
    public void shouldStartHelloWorldContainer() {

    }
}
