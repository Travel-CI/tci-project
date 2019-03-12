package com.travelci.logger.step.entities;

import com.travelci.logger.build.entities.Status;
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
public class StepDto {

    private Long id;

    @NotNull @NotEmpty
    private String command;

    private String commandResult;

    private Status status;

    @NotNull
    private Timestamp stepStart;

    private Timestamp stepEnd;

    private Long buildId;
}
