package com.travelci.docker.runner.services;

import com.travelci.docker.runner.entities.CommandDto;

import java.util.List;
import java.util.Map;

public interface DockerRunnerService {

    void startDockerRunnerEngine(List<CommandDto> commands);

    String buildImageFromDockerFile(String imageName, String dockerfileLocation);

    String startContainer(String imageId);

    Map<String, String> executeCommandsInContainer(String containerId, List<CommandDto> commands);

    void stopContainer(String containerId);
}
