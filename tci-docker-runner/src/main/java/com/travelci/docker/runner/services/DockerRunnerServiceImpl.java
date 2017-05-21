package com.travelci.docker.runner.services;

import com.travelci.docker.runner.entities.CommandDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DockerRunnerServiceImpl implements DockerRunnerService {

    @Override
    public void executeCommandsInDocker(List<CommandDto> commands) {

    }
}
