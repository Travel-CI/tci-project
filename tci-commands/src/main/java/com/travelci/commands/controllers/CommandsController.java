package com.travelci.commands.controllers;

import com.travelci.commands.entities.CommandDto;
import com.travelci.commands.exceptions.InvalidCommandException;
import com.travelci.commands.services.CommandsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/commands")
public class CommandsController {

    private CommandsService commandsService;

    public CommandsController(CommandsService commandsService) {
        this.commandsService = commandsService;
    }

    @GetMapping("{projectId}")
    public List<CommandDto> getCommandsByProject(@PathVariable final Integer projectId) {

        if (projectId == null)
            throw new InvalidCommandException();

        return commandsService.getCommandsByProject(projectId);
    }

    @PostMapping
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
}