package com.travelci.commands.services;

import com.travelci.commands.entities.CommandDto;
import com.travelci.commands.entities.ProjectDto;

import java.util.List;

public interface CommandsService {

    List<CommandDto> getCommandsByProject(Long projectId);

    CommandDto create(CommandDto commandDto);

    CommandDto update(CommandDto commandDto);

    void delete(CommandDto commandDto);

    void startCommandsEngine(ProjectDto projectDto);
}
