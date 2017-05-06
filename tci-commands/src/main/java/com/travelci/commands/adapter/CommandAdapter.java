package com.travelci.commands.adapter;

import com.travelci.commands.entities.CommandDto;
import com.travelci.commands.entities.CommandEntity;
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
                .order(command.getOrder())
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
                .order(commandDto.getOrder())
                .enabled(commandDto.getEnabled())
                .enableLogs(commandDto.getEnableLogs())
                .build()
            : null;
    }
}
