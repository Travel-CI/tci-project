package com.travelci.commands.adapter;

import com.travelci.commands.entities.CommandDto;
import com.travelci.commands.entities.CommandEntity;

public class CommandAdapter {

    public CommandDto toCommandDto(CommandEntity command) {
        return CommandDto.builder()
                .id(command.getId())
                .name(command.getName())
                .command(command.getCommand())
                .projectId(command.getProjectId())
                .order(command.getOrder())
                .enabled(command.getEnabled())
                .enableLogs(command.getEnableLogs())
                .build();
    }

    public CommandEntity toCommandEntity(CommandDto commandDto) {
        return CommandEntity.builder()
                .id(commandDto.getId())
                .name(commandDto.getName())
                .command(commandDto.getCommand())
                .projectId(commandDto.getProjectId())
                .order(commandDto.getOrder())
                .enabled(commandDto.getEnabled())
                .enableLogs(commandDto.getEnableLogs())
                .build();
    }
}
