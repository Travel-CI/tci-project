package com.travelci.projects.project.entities;

import com.travelci.projects.logger.entities.BuildDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {

    private Long id;

    @NotNull @NotEmpty
    private String name;

    private String description;

    @NotNull
    private Boolean enable;

    @NotNull @NotEmpty
    @Pattern(
        regexp = "((git|ssh|http(s)?)|(git@[\\w\\.]+))(:(//)?)([\\w\\.@\\:/\\-~]+)(\\.git)?(/)?",
        message = "Repository Url has wrong format"
    )
    private String repositoryUrl;

    @NotNull @NotEmpty
    private List<String> branches;

    private String userName;

    private String userPassword;

    private String repositoryToken;

    private String dockerfileLocation;

    private String emails;

    private Timestamp lastStart;

    private Timestamp created;

    private Timestamp updated;

    private BuildDto currentBuild;
}
