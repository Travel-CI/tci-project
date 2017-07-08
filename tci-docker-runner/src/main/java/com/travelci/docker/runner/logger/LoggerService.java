package com.travelci.docker.runner.logger;

import com.travelci.docker.runner.command.entities.CommandDto;
import com.travelci.docker.runner.logger.entities.BuildDto;
import com.travelci.docker.runner.logger.entities.StepDto;

public interface LoggerService {

    StepDto startNewStep(CommandDto command, BuildDto build);

    StepDto endStepByError(StepDto step, String commandOutput);

    StepDto endStepBySuccess(StepDto step, String commandOutput);

    BuildDto endBuildByError(BuildDto build, String error);

    BuildDto endBuildBySuccess(BuildDto build);
}
