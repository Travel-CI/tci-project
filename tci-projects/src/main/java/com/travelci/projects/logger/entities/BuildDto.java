package com.travelci.projects.logger.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuildDto {

    private Long id;

    @NotNull
    private Long projectId;

    @NotNull
    private Timestamp buildStart;

    private Timestamp buildEnd;

    @NotNull @NotEmpty
    private String startBy;

    @NotNull @NotEmpty
    private String commitHash;

    @NotNull @NotEmpty
    private String commitMessage;

    @NotNull @NotEmpty
    private String branch;

    private String error;
}
