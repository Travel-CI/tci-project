package com.travelci.logger.step.entities;

import org.springframework.stereotype.Component;

@Component
public class StepAdapter {

    public Step toStep(final StepDto stepDto) {
        return stepDto != null ?
            Step.builder()
                .id(stepDto.getId())
                .command(stepDto.getCommand())
                .commandResult(stepDto.getCommandResult())
                .status(stepDto.getStatus())
                .stepStart(stepDto.getStepStart())
                .stepEnd(stepDto.getStepEnd())
                .buildRoot(stepDto.getBuildRoot())
                .build()
            : null;
    }

    public StepDto toStepDto(final Step step) {
        return step != null ?
            StepDto.builder()
                .id(step.getId())
                .command(step.getCommand())
                .commandResult(step.getCommandResult())
                .status(step.getStatus())
                .stepStart(step.getStepStart())
                .stepEnd(step.getStepEnd())
                .buildRoot(step.getBuildRoot())
                .build()
            : null;
    }
}
