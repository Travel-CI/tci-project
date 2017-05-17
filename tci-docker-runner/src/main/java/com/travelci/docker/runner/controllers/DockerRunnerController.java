package com.travelci.docker.runner.controllers;

import com.travelci.docker.runner.services.DockerRunnerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/docker")
public class DockerRunnerController {

    private final DockerRunnerService dockerRunnerService;

    public DockerRunnerController(final DockerRunnerService dockerRunnerService) {
        this.dockerRunnerService = dockerRunnerService;
    }

    @GetMapping("execute")
    public void executeCommand() {
    }
}
