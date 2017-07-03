package com.travelci.docker.runner.services;

import com.travelci.docker.runner.entities.CommandDto;
import com.travelci.docker.runner.entities.DockerCommandsProject;

import java.util.List;
import java.util.Map;

public interface DockerRunnerService {

    void startDockerRunnerEngine(DockerCommandsProject dockerCommandsProject);

    String buildImageFromDockerFile(String imageName, String dockerfileLocation);

    String startContainer(String imageId, String projectFolder);

    Map<String, String> executeCommandsInContainer(String containerId, List<CommandDto> commands);

    void stopContainer(String containerId);
}
