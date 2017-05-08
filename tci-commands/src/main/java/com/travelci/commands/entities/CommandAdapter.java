package com.travelci.commands.entities;

import org.springframework.stereotype.Component;

@Component
public class CommandAdapter {

    public CommandDto toCommandDto(final CommandEntity command) {
        return command != null ?
            CommandDto.builder()
                .id(command.getId())
                .name(command.getName())
                .command(command.getCommand())
                .projectId(command.getProjectId())
                .commandOrder(command.getCommandOrder())
                .enabled(command.getEnabled())
                .enableLogs(command.getEnableLogs())
                .build()
            : null;
    }

    public CommandEntity toCommandEntity(final CommandDto commandDto) {
        return commandDto != null ?
            CommandEntity.builder()
                .id(commandDto.getId())
                .name(commandDto.getName())
                .command(commandDto.getCommand())
                .projectId(commandDto.getProjectId())
                .commandOrder(commandDto.getCommandOrder())
                .enabled(commandDto.getEnabled())
                .enableLogs(commandDto.getEnableLogs())
                .build()
            : null;
    }
}
