package com.travelci.docker.runner.logger.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StepDto {

    private Long id;

    @NotNull @NotEmpty
    private String command;

    private String commandResult;
    
    @NotNull
    private Timestamp stepStart;

    private Timestamp stepEnd;

    private BuildDto buildRoot;
}
