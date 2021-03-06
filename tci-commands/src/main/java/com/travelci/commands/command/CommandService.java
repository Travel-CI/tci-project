package com.travelci.commands.command;

import com.travelci.commands.command.entities.CommandDto;
import com.travelci.commands.project.entities.ProjectDto;

import java.util.List;

interface CommandService {

    List<CommandDto> getCommandsByProjectId(Long projectId);

    List<CommandDto> create(List<CommandDto> commands);

    List<CommandDto> update(List<CommandDto> commands, Long projectId);

    void startCommandsEngine(ProjectDto project);
}
