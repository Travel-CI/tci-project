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
                .stepStatus(stepDto.getStepStatus())
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
                .stepStatus(step.getStepStatus())
                .stepStart(step.getStepStart())
                .stepEnd(step.getStepEnd())
                .buildRoot(step.getBuildRoot())
                .build()
            : null;
    }
}
