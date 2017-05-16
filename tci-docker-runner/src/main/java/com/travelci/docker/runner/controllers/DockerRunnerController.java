package com.travelci.docker.runner.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/docker")
public class DockerRunnerController {

    @GetMapping("execute")
    public void executeCommand() {
    }
}
