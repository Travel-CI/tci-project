package com.travelci.commands.command;

import com.travelci.commands.command.entities.CommandDto;
import com.travelci.commands.command.exceptions.InvalidCommandException;
import com.travelci.commands.project.entities.ProjectDto;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/commands")
public class CommandController {

    private final CommandService commandService;
    private final Validator validator;

    public CommandController(final CommandService commandService, final Validator validator) {
        this.commandService = commandService;
        this.validator = validator;
    }

    @GetMapping("{projectId}")
    public List<CommandDto> getCommandsByProject(@PathVariable final Long projectId) {
        return commandService.getCommandsByProjectId(projectId);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public List<CommandDto> createNewCommands(@Valid @RequestBody final List<CommandDto> commands,
                                       final BindingResult bindingResult) {

        if (bindingResult.hasErrors() || commands.isEmpty())
            throw new InvalidCommandException();

        // Custom Validator for Command inside List
        // List Object is not a Java Bean, @Valid does not work above
        for (final CommandDto command : commands) {
            final Set<ConstraintViolation<CommandDto>> constraintViolations = validator.validate(command);
            if (!constraintViolations.isEmpty())
                throw new InvalidCommandException();
        }

        return commandService.create(commands);
    }

    @PutMapping
    public CommandDto updateCommand(@Valid @RequestBody final CommandDto command,
                                    final BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            throw new InvalidCommandException();

        return commandService.update(command);
    }

    @DeleteMapping
    public void deleteCommand(@Valid @RequestBody final CommandDto command,
                              final BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            throw new InvalidCommandException();

        commandService.delete(command);
    }

    @PostMapping("/project")
    @ResponseStatus(ACCEPTED)
    public void getCommandsAndSendToDockerRunner(@Valid @RequestBody final ProjectDto project,
                                                 final BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            throw new InvalidCommandException();

        commandService.startCommandsEngine(project);
    }
}
