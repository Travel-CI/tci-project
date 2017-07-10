package com.travelci.commands.command;

import com.travelci.commands.command.entities.Command;
import com.travelci.commands.command.entities.CommandAdapter;
import com.travelci.commands.command.entities.CommandDto;
import com.travelci.commands.command.exceptions.InvalidCommandException;
import com.travelci.commands.command.exceptions.NotFoundCommandException;
import com.travelci.commands.docker.entities.DockerCommandsProject;
import com.travelci.commands.logger.LoggerService;
import com.travelci.commands.project.entities.ProjectDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.ACCEPTED;

@Service
@RefreshScope
@Transactional
class CommandServiceImpl implements CommandService {

    private final CommandRepository commandRepository;
    private final CommandAdapter commandAdapter;
    private final RestTemplate restTemplate;
    private final LoggerService loggerService;

    private final String dockerRunnerServiceUrl;

    CommandServiceImpl(final CommandRepository commandRepository, final CommandAdapter commandAdapter,
                       final RestTemplate restTemplate, final LoggerService loggerService,
                       @Value("${info.services.docker-runner}") final String dockerRunnerServiceUrl) {
        this.commandRepository = commandRepository;
        this.commandAdapter = commandAdapter;
        this.restTemplate = restTemplate;
        this.loggerService = loggerService;
        this.dockerRunnerServiceUrl = dockerRunnerServiceUrl;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommandDto> getCommandsByProjectId(final Long projectId) {
        return commandRepository.findByProjectIdOrderByCommandOrderAsc(projectId)
            .stream()
            .map(commandAdapter::toCommandDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<CommandDto> create(final List<CommandDto> commands) {

        final List<Command> commandList = commands
            .stream()
            .map(commandAdapter::toCommand)
            .collect(Collectors.toList());

        for (Command command : commandList)
            if (commandRepository.findByProjectIdAndCommandOrder(
                command.getProjectId(), command.getCommandOrder()).isPresent())
                throw new InvalidCommandException("CommandOrder already exist for this project");

        return commandRepository.save(commandList)
            .stream()
            .map(commandAdapter::toCommandDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<CommandDto> update(final List<CommandDto> updatedCommands, final Long projectId) {

        final List<Command> existingCommands = commandRepository.findByProjectIdOrderByCommandOrderAsc(projectId);

        if (updatedCommands.isEmpty()) {
            commandRepository.delete(existingCommands);
            return new ArrayList<>();
        }

        for (final Command existingCommand : existingCommands) {
            Boolean found = false;

            for (final CommandDto updatedCommand : updatedCommands) {
                if (existingCommand.getId().equals(updatedCommand.getId())) {
                    found = true;
                    break;
                }
            }

            if(!found)
                commandRepository.delete(existingCommand);
        }

        final List<Command> commands = updatedCommands.stream()
            .map(commandAdapter::toCommand)
            .collect(Collectors.toList());

        return commandRepository.save(commands)
            .stream()
            .map(commandAdapter::toCommandDto)
            .collect(Collectors.toList());
    }

    @Override
    public void delete(final CommandDto command) {

        commandRepository.findById(command.getId())
            .orElseThrow(NotFoundCommandException::new);

        commandRepository.delete(commandAdapter.toCommand(command));
    }

    @Override
    public void startCommandsEngine(final ProjectDto project) {

        final List<CommandDto> commands = commandRepository
            .findByProjectIdAndEnabledIsTrueOrderByCommandOrderAsc(project.getId())
            .stream()
            .map(commandAdapter::toCommandDto)
            .collect(Collectors.toList());

        if (commands.isEmpty()) {
            final RuntimeException e = new NotFoundCommandException("There is no command to execute");
            loggerService.endBuildByError(project.getCurrentBuild(), e);
            throw e;
        }

        final DockerCommandsProject dockerCommandsProject = DockerCommandsProject.builder()
            .project(project)
            .commands(commands)
            .build();

        try {
            final ResponseEntity<Void> response = restTemplate.postForEntity(
                dockerRunnerServiceUrl + "/docker/execute",
                dockerCommandsProject, Void.class
            );

            if (!ACCEPTED.equals(response.getStatusCode()))
                throw new RestClientException(
                    "Response Status Code is wrong. Expected : ACCEPTED, Given : " + response.getStatusCode());
        } catch (final RestClientException e) {
            loggerService.endBuildByError(project.getCurrentBuild(), e);
        }
    }
}
