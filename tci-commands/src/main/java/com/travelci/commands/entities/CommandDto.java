package com.travelci.commands.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandDto {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String command;

    @NotNull
    private Long projectId;

    @NotNull
    private Integer commandOrder;

    @NotNull
    private Boolean enabled;

    @NotNull
    private Boolean enableLogs;
}
