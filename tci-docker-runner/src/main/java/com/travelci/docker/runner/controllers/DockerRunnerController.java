package com.travelci.docker.runner.controllers;

import com.travelci.docker.runner.entities.CommandDto;
import com.travelci.docker.runner.exceptions.WrongFormatCommandException;
import com.travelci.docker.runner.services.DockerRunnerService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    public void executeCommand(@Valid @RequestBody final List<CommandDto> commands,
                               final BindingResult bindingResult) {

        if (bindingResult.hasErrors() /*|| commands.isEmpty()*/)
            throw new WrongFormatCommandException();

        dockerRunnerService.executeCommandsInDocker(commands);
    }
}
