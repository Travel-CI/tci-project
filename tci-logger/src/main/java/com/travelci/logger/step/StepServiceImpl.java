package com.travelci.logger.step;

import com.travelci.logger.step.entities.StepAdapter;
import com.travelci.logger.step.entities.StepDto;
import com.travelci.logger.step.exceptions.InvalidStepException;
import com.travelci.logger.step.exceptions.NotFoundStepException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.travelci.logger.build.entities.Status.ERROR;
import static com.travelci.logger.build.entities.Status.IN_PROGRESS;
import static com.travelci.logger.build.entities.Status.SUCCESS;

import java.util.List;
import java.util.stream.Collectors;

import static com.travelci.logger.build.entities.Status.ERROR;
import static com.travelci.logger.build.entities.Status.IN_PROGRESS;
import static com.travelci.logger.build.entities.Status.SUCCESS;

@Service
@Transactional
class StepServiceImpl implements StepService {

    private final StepRepository stepRepository;
    private final StepAdapter stepAdapter;

    StepServiceImpl(final StepRepository stepRepository,
                    final StepAdapter stepAdapter) {
        this.stepRepository = stepRepository;
        this.stepAdapter = stepAdapter;
    }

    @Override
    public StepDto create(final StepDto step) {

        step.setStatus(IN_PROGRESS);

        return stepAdapter.toStepDto(
            stepRepository.save(stepAdapter.toStep(step))
        );
    }

    @Override
    public StepDto endBySuccess(final StepDto step) {

        checkStep(step);
        step.setStatus(SUCCESS);

        return stepAdapter.toStepDto(
            stepRepository.save(stepAdapter.toStep(step))
        );
    }

    @Override
    public StepDto endByError(final StepDto step) {

        checkStep(step);
        step.setStatus(ERROR);

        return stepAdapter.toStepDto(
            stepRepository.save(stepAdapter.toStep(step))
        );
    }

    @Override
    public List<StepDto> getStepsByBuildId(final Long buildId) {
        return stepRepository.findByBuildId(buildId)
            .stream()
            .map(stepAdapter::toStepDto)
            .collect(Collectors.toList());
    }

    private void checkStep(final StepDto step) {

        stepRepository.findById(step.getId())
            .orElseThrow(NotFoundStepException::new);

        if (step.getStepEnd() == null)
            throw new InvalidStepException();
    }
}
