package com.travelci.commands.services;

import com.travelci.commands.adapter.CommandAdapter;
import com.travelci.commands.entities.CommandDto;
import com.travelci.commands.entities.CommandEntity;
import com.travelci.commands.repository.CommandRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CommandsServiceImpl implements CommandsService {

    private final CommandRepository commandRepository;

    private final CommandAdapter commandAdapter;

    public CommandsServiceImpl(final CommandRepository commandRepository, final CommandAdapter commandAdapter) {
        this.commandRepository = commandRepository;
        this.commandAdapter = commandAdapter;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommandDto> getCommandsByProject(final Integer projectId) {

        List<CommandDto> commandDtoList = new ArrayList();
        List<CommandEntity> resultQuery = commandRepository.findCommandEntitiesByProjectId(projectId);

        for(CommandEntity commandEntity : resultQuery) {
            commandDtoList.add(commandAdapter.toCommandDto(commandEntity));
        }

        return commandDtoList;
    }

    @Override
    public CommandDto create(final CommandDto commandDto) {
        return commandAdapter.toCommandDto(commandRepository.save(commandAdapter.toCommandEntity(commandDto)));
    }

    @Override
    public CommandDto update(final CommandDto commandDto) {
        return commandAdapter.toCommandDto(commandRepository.save(commandAdapter.toCommandEntity(commandDto)));
    }

    @Override
    public void delete(final CommandDto commandDto) {
        commandRepository.delete(commandAdapter.toCommandEntity(commandDto));
    }

    @Override
    public void executeCommand(final CommandDto command) {

    }
}
