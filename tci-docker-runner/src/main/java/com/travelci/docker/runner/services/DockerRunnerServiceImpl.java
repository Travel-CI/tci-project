package com.travelci.docker.runner.services;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.travelci.docker.runner.entities.CommandDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DockerRunnerServiceImpl implements DockerRunnerService {

    private final DockerClient dockerClient;

    public DockerRunnerServiceImpl(final DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    @Override
    public void executeCommandsInDocker(final List<CommandDto> commands) {

        try {
            dockerClient.pull("busybox:1");
            dockerClient.close();
        } catch (DockerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
