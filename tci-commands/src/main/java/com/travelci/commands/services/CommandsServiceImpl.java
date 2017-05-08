package com.travelci.commands.services;

import com.travelci.commands.entities.CommandAdapter;
import com.travelci.commands.entities.CommandDto;
import com.travelci.commands.exceptions.InvalidCommandException;
import com.travelci.commands.exceptions.NotFoundCommandException;
import com.travelci.commands.repository.CommandRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommandsServiceImpl implements CommandsService {

    private final CommandRepository commandRepository;

    private final CommandAdapter commandAdapter;

    public CommandsServiceImpl(final CommandRepository commandRepository,
                               final CommandAdapter commandAdapter) {
        this.commandRepository = commandRepository;
        this.commandAdapter = commandAdapter;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommandDto> getCommandsByProject(final Long projectId) {
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
            commandRepository.save(commandAdapter.toCommandEntity(commandDto))
        );
    }

    @Override
    public CommandDto update(final CommandDto commandDto) {

        commandRepository.findById(commandDto.getId())
            .orElseThrow(NotFoundCommandException::new);

        return commandAdapter.toCommandDto(
            commandRepository.save(commandAdapter.toCommandEntity(commandDto))
        );
    }

    @Override
    public void delete(final CommandDto commandDto) {

        commandRepository.findById(commandDto.getId())
            .orElseThrow(NotFoundCommandException::new);

        commandRepository.delete(commandAdapter.toCommandEntity(commandDto));
    }

    @Override
    public void executeCommand(final CommandDto command) {

    }
}
