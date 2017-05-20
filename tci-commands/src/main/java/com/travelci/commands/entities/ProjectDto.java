package com.travelci.commands.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Builder
@Data
@AllArgsConstructor
public class ProjectDto {

    @NotNull
    private Long id;

    @NotNull @NotEmpty
    private String name;

    private String description;

    @NotNull
    private Boolean enable;

    @NotNull @NotEmpty
    @Pattern(
        regexp = "((git|ssh|http(s)?)|(git@[\\w\\.]+))(:(//)?)([\\w\\.@\\:/\\-~]+)(\\.git)(/)?",
        message = "Repository Url has wrong format"
    )
    private String repositoryUrl;
}
