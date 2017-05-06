package com.travelci.projects.entities;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Builder
@Data
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

    private Timestamp created;

    private Timestamp updated;
}
