package com.travelci.docker.runner.services;

import com.travelci.docker.runner.entities.CommandDto;

import java.util.List;

public interface DockerRunnerService {

    String buildImageFromDockerFile(String imageName, String dockerfileLocation);

    String startContainer(String imageId);

    void executeCommandsInContainer(List<CommandDto> commands);

    void stopContainer(String containerId);
}
