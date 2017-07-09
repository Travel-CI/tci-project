package com.travelci.docker.runner.docker;

import com.travelci.docker.runner.command.entities.DockerCommandsProject;
import com.travelci.docker.runner.command.exceptions.WrongFormatException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.ACCEPTED;

@RestController
@RequestMapping("/docker")
public class DockerRunnerController {

    private final DockerRunnerService dockerRunnerService;

    public DockerRunnerController(final DockerRunnerService dockerRunnerService) {
        this.dockerRunnerService = dockerRunnerService;
    }

    @PostMapping("execute")
    @ResponseStatus(ACCEPTED)
    public void executeCommand(@Valid @RequestBody final DockerCommandsProject dockerCommandsProject,
                               final BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            throw new WrongFormatException();

        dockerRunnerService.startDockerRunnerEngine(dockerCommandsProject);
    }
}
