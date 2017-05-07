package com.travelci.projects.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;
import java.util.Date;
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
        regexp = "((git|ssh|http(s)?)|(git@[\\w\\.]+))(:(//)?)([\\w\\.@\\:/\\-~]+)(\\.git)(/)?",
        message = "Repository Url has wrong format"
    )
    private String repositoryUrl;

    @NotNull @NotEmpty
    private List<String> branches;

    private String userName;

    private String userPassword;

    private String repositoryToken;

    //@NotNull
    private String dockerfileLocation;

    private Date lastStart;

    private Timestamp created;

    private Timestamp updated;
}
