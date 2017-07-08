package com.travelci.docker.runner.command.entities;

import com.travelci.docker.runner.project.entities.ProjectDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DockerCommandsProject {

    @NotNull
    private ProjectDto project;

    @NotNull @NotEmpty
    private List<CommandDto> commands;
}
