package com.travelci.docker.runner.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {

    @NotNull
    private Long id;

    @NotNull @NotEmpty
    private String name;

    @NotNull @NotEmpty
    @Pattern(
        regexp = "((git|ssh|http(s)?)|(git@[\\w\\.]+))(:(//)?)([\\w\\.@\\:/\\-~]+)(\\.git)?(/)?",
        message = "Repository Url has wrong format"
    )
    private String repositoryUrl;

    private String dockerfileLocation;
}
