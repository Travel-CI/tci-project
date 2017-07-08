package com.travelci.logger.step;

import com.travelci.logger.step.entities.StepDto;

import java.util.List;

interface StepService {

    StepDto create(StepDto step);

    StepDto endBySuccess(StepDto step);

    StepDto endByError(StepDto step);

    List<StepDto> getStepsByBuild(Long buildId);
}
