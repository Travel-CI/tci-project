package com.travelci.commands.project.entities;

import com.travelci.commands.logger.entities.BuildDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {

    @NotNull
    private Long id;

    @NotNull @NotEmpty
    private String name;

    private String dockerfileLocation;

    private String emails;

    @NotNull
    private BuildDto currentBuild;
}
