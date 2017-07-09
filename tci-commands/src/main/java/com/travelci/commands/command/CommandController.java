package com.travelci.commands.command;

import com.travelci.commands.command.entities.CommandDto;
import com.travelci.commands.project.entities.ProjectDto;
import com.travelci.commands.command.exceptions.InvalidCommandException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/commands")
public class CommandController {

    private final CommandService commandService;

    public CommandController(final CommandService commandService) {
        this.commandService = commandService;
    }

    @GetMapping("{projectId}")
    public List<CommandDto> getCommandsByProject(@PathVariable final Long projectId) {
        return commandService.getCommandsByProjectId(projectId);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public CommandDto createNewCommand(@Valid @RequestBody final CommandDto commandDto,
                                       final BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            throw new InvalidCommandException();

        return commandService.create(commandDto);
    }

    @PutMapping
    public CommandDto updateCommand(@Valid @RequestBody final CommandDto commandDto,
                                    final BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            throw new InvalidCommandException();

        return commandService.update(commandDto);
    }

    @DeleteMapping
    public void deleteCommand(@Valid @RequestBody final CommandDto commandDto,
                              final BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            throw new InvalidCommandException();

        commandService.delete(commandDto);
    }

    @PostMapping("/project")
    @ResponseStatus(ACCEPTED)
    public void getCommandsAndSendToDockerRunner(@Valid @RequestBody final ProjectDto projectDto,
                                                 final BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            throw new InvalidCommandException();

        commandService.startCommandsEngine(projectDto);
    }
}