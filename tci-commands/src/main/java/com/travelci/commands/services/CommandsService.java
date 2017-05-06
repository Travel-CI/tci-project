package com.travelci.commands.services;

import com.travelci.commands.entities.CommandDto;

import java.util.List;

public interface CommandsService {

    List<CommandDto> getCommandsByProject(Long projectId);

    CommandDto create(CommandDto commandDto);

    CommandDto update(CommandDto commandDto);

    void delete(CommandDto commandDto);

    void executeCommand(CommandDto command);
}
