package com.travelci.commands.controllers;

import com.travelci.commands.entities.CommandDto;
import com.travelci.commands.entities.ProjectDto;
import com.travelci.commands.exceptions.InvalidCommandException;
import com.travelci.commands.services.CommandsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/commands")
public class CommandsController {

    private final CommandsService commandsService;

    public CommandsController(final CommandsService commandsService) {
        this.commandsService = commandsService;
    }

    @GetMapping("{projectId}")
    public List<CommandDto> getCommandsByProject(@PathVariable final Long projectId) {
        return commandsService.getCommandsByProject(projectId);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public CommandDto createNewCommand(@Valid @RequestBody final CommandDto commandDto,
                                       final BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            throw new InvalidCommandException();

        return commandsService.create(commandDto);
    }

    @PutMapping
    public CommandDto updateCommand(@Valid @RequestBody final CommandDto commandDto,
                                    final BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            throw new InvalidCommandException();

        return commandsService.update(commandDto);
    }

    @DeleteMapping
    public void deleteCommand(@Valid @RequestBody final CommandDto commandDto,
                              final BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            throw new InvalidCommandException();

        commandsService.delete(commandDto);
    }

    @PostMapping("/project")
    @ResponseStatus(ACCEPTED)
    public void getCommandsAndSendToDockerRunner(@Valid @RequestBody final ProjectDto projectDto,
                                                 final BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            throw new InvalidCommandException();

        commandsService.startCommandsEngine(projectDto);
    }
}
