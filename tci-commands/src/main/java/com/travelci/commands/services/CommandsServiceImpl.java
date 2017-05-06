package com.travelci.commands.services;

import com.travelci.commands.adapter.CommandAdapter;
import com.travelci.commands.entities.CommandDto;
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
        return commandRepository.findByProjectId(projectId)
            .stream()
            .map(commandAdapter::toCommandDto)
            .collect(Collectors.toList());
    }

    @Override
    public CommandDto create(final CommandDto commandDto) {
        return commandAdapter.toCommandDto(
            commandRepository.save(commandAdapter.toCommandEntity(commandDto))
        );
    }

    @Override
    public CommandDto update(final CommandDto commandDto) {
        return commandAdapter.toCommandDto(
            commandRepository.save(commandAdapter.toCommandEntity(commandDto))
        );
    }

    @Override
    public void delete(final CommandDto commandDto) {
        commandRepository.delete(commandAdapter.toCommandEntity(commandDto));
    }

    @Override
    public void executeCommand(final CommandDto command) {

    }
}
