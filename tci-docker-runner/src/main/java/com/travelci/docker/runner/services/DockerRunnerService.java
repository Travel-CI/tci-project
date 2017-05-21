package com.travelci.docker.runner.services;

import com.travelci.docker.runner.entities.CommandDto;

import java.util.List;

public interface DockerRunnerService {

    void executeCommandsInDocker(List<CommandDto> commands);
}
