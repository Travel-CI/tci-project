package com.travelci.commands.services;

import com.travelci.commands.entities.CommandDto;

import java.util.List;

public interface CommandsService {

    List<CommandDto> getCommandsByProject(final Integer projectId);

    CommandDto create(final CommandDto commandDto);

    CommandDto update(final CommandDto commandDto);

    void delete(final CommandDto commandDto);

    void executeCommand(final CommandDto command);
}
