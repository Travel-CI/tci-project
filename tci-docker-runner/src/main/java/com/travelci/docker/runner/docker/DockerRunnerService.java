package com.travelci.docker.runner.docker;

import com.travelci.docker.runner.command.entities.CommandDto;
import com.travelci.docker.runner.command.entities.DockerCommandsProject;
import com.travelci.docker.runner.logger.entities.BuildDto;
import com.travelci.docker.runner.project.entities.ProjectDto;

import java.util.List;
import java.util.Map;

interface DockerRunnerService {

    void startDockerRunnerEngine(DockerCommandsProject dockerCommandsProject);

    String buildImageFromDockerFile(String imageName, String dockerfileLocation);

    String startContainer(String imageId, String projectFolder);

    Map<String, String> executeCommandsInContainer(String containerId, List<CommandDto> commands,
                                                   ProjectDto project);

    void stopContainer(String containerId);
}
