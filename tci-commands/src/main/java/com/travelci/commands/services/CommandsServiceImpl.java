package com.travelci.commands.services;

import com.travelci.commands.entities.CommandDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommandsServiceImpl implements CommandsService {

    @Override
    @Transactional(readOnly = true)
    public List<CommandDto> getCommandsByProject(final Integer projectId) {
        return null;
    }

    @Override
    public CommandDto create(final CommandDto commandDto) {
        return null;
    }

    @Override
    public CommandDto update(final CommandDto commandDto) {
        return null;
    }

    @Override
    public void delete(final CommandDto commandDto) {

    }

    @Override
    public void executeCommand(final CommandDto command) {

    }
}
