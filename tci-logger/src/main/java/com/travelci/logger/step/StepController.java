package com.travelci.logger.step;

import com.travelci.logger.build.exceptions.InvalidBuildException;
import com.travelci.logger.step.entities.StepDto;
import com.travelci.logger.step.exceptions.InvalidStepException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/steps")
public class StepController {

    private final StepService stepService;

    public StepController(final StepService stepService) {
        this.stepService = stepService;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public StepDto createNewStep(@Valid @RequestBody final StepDto step,
                                 final BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            throw new InvalidStepException();

        return stepService.create(step);
    }

    @PutMapping("success")
    public StepDto endStepBySuccess(@Valid @RequestBody final StepDto step,
                                      final BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            throw new InvalidBuildException();

        return stepService.endBySuccess(step);
    }

    @PutMapping("error")
    public StepDto endStepByError(@Valid @RequestBody final StepDto step,
                                    final BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            throw new InvalidBuildException();

        return stepService.endByError(step);
    }

    @GetMapping("{buildId}")
    public List<StepDto> getAllStepsForBuild(@PathVariable("buildId") final Long buildId) {
        return stepService.getStepsByBuild(buildId);
    }
}
