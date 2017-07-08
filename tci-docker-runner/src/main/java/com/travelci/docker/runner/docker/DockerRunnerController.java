package com.travelci.docker.runner.docker;

import com.travelci.docker.runner.input.entities.DockerCommandsProject;
import com.travelci.docker.runner.input.exceptions.WrongFormatInputException;
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
            throw new WrongFormatInputException();

        dockerRunnerService.startDockerRunnerEngine(dockerCommandsProject);
    }
}
