package com.travelci.projects.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
public class PayLoad {

    @NotNull @NotEmpty
    private String repositoryUrl;

    @NotNull @NotEmpty
    private String branchName;

    @NotNull @NotEmpty
    private String commitAuthor;

    @NotNull @NotEmpty
    private String commitHash;

    @NotNull @NotEmpty
    private String commitMessage;
}