package com.travelci.commands.entities;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Builder
@Data
public class CommandDto {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String command;

    @NotNull
    private Integer projectId;

    @NotNull
    private Integer order;

    @NotNull
    private Boolean enabled;

    @NotNull
    private Boolean enableLogs;
}
