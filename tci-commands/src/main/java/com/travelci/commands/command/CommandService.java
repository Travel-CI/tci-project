package com.travelci.commands.command;

import com.travelci.commands.command.entities.CommandDto;
import com.travelci.commands.project.entities.ProjectDto;

import java.util.List;

interface CommandService {

    List<CommandDto> getCommandsByProjectId(Long projectId);

    List<CommandDto> create(List<CommandDto> commands);

    CommandDto update(CommandDto command);

    void delete(CommandDto command);

    void startCommandsEngine(ProjectDto project);
}
