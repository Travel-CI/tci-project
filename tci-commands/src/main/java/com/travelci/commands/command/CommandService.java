package com.travelci.commands.command;

import com.travelci.commands.command.entities.CommandDto;
import com.travelci.commands.project.entities.ProjectDto;

import java.util.List;

interface CommandService {

    List<CommandDto> getCommandsByProjectId(Long projectId);

    CommandDto create(CommandDto commandDto);

    CommandDto update(CommandDto commandDto);

    void delete(CommandDto commandDto);

    void startCommandsEngine(ProjectDto projectDto);
}
