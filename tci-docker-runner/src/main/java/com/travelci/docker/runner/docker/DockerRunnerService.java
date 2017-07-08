package com.travelci.docker.runner.docker;

import com.travelci.docker.runner.input.entities.CommandDto;
import com.travelci.docker.runner.input.entities.DockerCommandsProject;

import java.util.List;
import java.util.Map;

interface DockerRunnerService {

    void startDockerRunnerEngine(DockerCommandsProject dockerCommandsProject);

    String buildImageFromDockerFile(String imageName, String dockerfileLocation);

    String startContainer(String imageId, String projectFolder);

    Map<String, String> executeCommandsInContainer(String containerId, List<CommandDto> commands);

    void stopContainer(String containerId);
}
