package com.travelci.projects.entities;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Builder
@Data
public class ProjectDto {

    private Long id;

    @NotNull @NotEmpty
    private String name;

    private String description;

    @NotNull
    private Boolean enable;

    @NotNull
    private String repositoryUrl;

    @NotNull
    private List<String> branches;

    private String userName;

    private String userPassword;

    private String repositoryToken;

    @NotNull
    private String dockerFileLocation;

    private Date lastStart;

    private Timestamp created;

    private Timestamp updated;
}
