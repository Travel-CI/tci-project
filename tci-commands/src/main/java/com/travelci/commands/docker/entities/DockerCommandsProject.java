package com.travelci.commands.docker.entities;

import com.travelci.commands.command.entities.CommandDto;
import com.travelci.commands.project.entities.ProjectDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DockerCommandsProject {

    private ProjectDto project;

    private List<CommandDto> commands;
}
