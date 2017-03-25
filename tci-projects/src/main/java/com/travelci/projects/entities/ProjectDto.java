package com.travelci.projects.entities;

import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Builder
public class ProjectDto {

    private Long id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private Boolean enable;

    @NotNull
    private String repositoryUrl;

    @NotNull
    private List<String> branches;

    @NotNull
    private String dockerFileLocation;

    private Date lastStart;
}
