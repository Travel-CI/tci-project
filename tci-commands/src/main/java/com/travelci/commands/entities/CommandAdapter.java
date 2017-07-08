package com.travelci.commands.entities;

import org.springframework.stereotype.Component;

@Component
public class CommandAdapter {

    public CommandDto toCommandDto(final Command command) {
        return command != null ?
            CommandDto.builder()
                .id(command.getId())
                .command(command.getCommand())
                .projectId(command.getProjectId())
                .commandOrder(command.getCommandOrder())
                .enabled(command.getEnabled())
                .enableLogs(command.getEnableLogs())
                .build()
            : null;
    }

    public Command toCommand(final CommandDto commandDto) {
        return commandDto != null ?
            Command.builder()
                .id(commandDto.getId())
                .command(commandDto.getCommand())
                .projectId(commandDto.getProjectId())
                .commandOrder(commandDto.getCommandOrder())
                .enabled(commandDto.getEnabled())
                .enableLogs(commandDto.getEnableLogs())
                .build()
            : null;
    }
}
