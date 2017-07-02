package com.travelci.commands.entities;

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
