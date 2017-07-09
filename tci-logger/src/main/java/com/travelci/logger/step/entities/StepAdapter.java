package com.travelci.logger.step.entities;

import com.travelci.logger.build.entities.BuildAdapter;
import org.springframework.stereotype.Component;

@Component
public class StepAdapter {

    private final BuildAdapter buildAdapter;

    public StepAdapter() {
        this.buildAdapter = new BuildAdapter(null);
    }

    public Step toStep(final StepDto stepDto) {
        return stepDto != null ?
            Step.builder()
                .id(stepDto.getId())
                .command(stepDto.getCommand())
                .commandResult(stepDto.getCommandResult())
                .status(stepDto.getStatus())
                .stepStart(stepDto.getStepStart())
                .stepEnd(stepDto.getStepEnd())
                .buildRoot(
                    buildAdapter.toBuild(stepDto.getBuildRoot())
                )
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
                .buildRoot(buildAdapter.toBuildDto(step.getBuildRoot()))
                .build()
            : null;
    }
}
