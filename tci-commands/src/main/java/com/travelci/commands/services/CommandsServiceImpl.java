package com.travelci.commands.services;

import com.travelci.commands.entities.CommandAdapter;
import com.travelci.commands.entities.CommandDto;
import com.travelci.commands.entities.DockerCommandsProject;
import com.travelci.commands.entities.ProjectDto;
import com.travelci.commands.exceptions.InvalidCommandException;
import com.travelci.commands.exceptions.NotFoundCommandException;
import com.travelci.commands.repository.CommandRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.ACCEPTED;

@Service
@RefreshScope
@Transactional
public class CommandsServiceImpl implements CommandsService {

    private final CommandRepository commandRepository;
    private final CommandAdapter commandAdapter;
    private final RestTemplate restTemplate;

    private final String dockerRunnerServiceUrl;

    public CommandsServiceImpl(final CommandRepository commandRepository,
                               final CommandAdapter commandAdapter,
                               final RestTemplate restTemplate,
                               @Value("${info.services.docker-runner}")
                               final String dockerRunnerServiceUrl) {
        this.commandRepository = commandRepository;
        this.commandAdapter = commandAdapter;
        this.restTemplate = restTemplate;
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
    public CommandDto create(final CommandDto commandDto) {

        if (commandRepository.findByProjectIdAndCommandOrder(
            commandDto.getProjectId(), commandDto.getCommandOrder()).isPresent())
            throw new InvalidCommandException("CommandOrder already exist for this project");

        return commandAdapter.toCommandDto(
            commandRepository.save(commandAdapter.toCommand(commandDto))
        );
    }

    @Override
    public CommandDto update(final CommandDto commandDto) {

        commandRepository.findById(commandDto.getId())
            .orElseThrow(NotFoundCommandException::new);

        return commandAdapter.toCommandDto(
            commandRepository.save(commandAdapter.toCommand(commandDto))
        );
    }

    @Override
    public void delete(final CommandDto commandDto) {

        commandRepository.findById(commandDto.getId())
            .orElseThrow(NotFoundCommandException::new);

        commandRepository.delete(commandAdapter.toCommand(commandDto));
    }

    @Override
    public void startCommandsEngine(final ProjectDto projectDto) {

        final List<CommandDto> commands = getCommandsByProjectId(projectDto.getId());

        if (commands.isEmpty()) {
            // TODO Log end Build with error
            throw new NotFoundCommandException();
        }

        final DockerCommandsProject dockerCommandsProject = DockerCommandsProject.builder()
            .project(projectDto)
            .commands(commands)
            .build();

        try {
            final ResponseEntity<Void> response = restTemplate.postForEntity(
                dockerRunnerServiceUrl + "/docker/execute",
                dockerCommandsProject,
                Void.class
            );

            if (!ACCEPTED.equals(response.getStatusCode()))
                throw new RestClientException(
                    "Response Status Code is wrong. Expected : ACCEPTED, Given : " + response.getStatusCode()
                );
        } catch (RestClientException e) {
            System.out.println("Wrong request" + e.getLocalizedMessage());
            e.printStackTrace();
            // TODO Call logger service
        }
    }
}
