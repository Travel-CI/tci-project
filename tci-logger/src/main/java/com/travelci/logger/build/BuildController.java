package com.travelci.logger.build;

import com.travelci.logger.build.entities.BuildDto;
import com.travelci.logger.build.exceptions.InvalidBuildException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("builds")
public class BuildController {

    private final BuildService buildService;

    public BuildController(final BuildService buildService) {
        this.buildService = buildService;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public BuildDto createNewBuild(@Valid @RequestBody final BuildDto build,
                               final BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            throw new InvalidBuildException();

        return buildService.create(build);
    }

    @PutMapping("success")
    public BuildDto endBuildBySuccess(@Valid @RequestBody final BuildDto build,
                                      final BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            throw new InvalidBuildException();

        return buildService.endBySuccess(build);
    }

    @PutMapping("error")
    public BuildDto endBuildByError(@Valid @RequestBody final BuildDto build,
                                      final BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            throw new InvalidBuildException();

        return buildService.endByError(build);
    }

    @GetMapping("{projectId}")
    public List<BuildDto> getAllBuildForProject(@PathVariable("projectId") final Long projectId) {
        return buildService.getBuildsByProjectId(projectId);
    }

    @GetMapping("last/{projectId}")
    public BuildDto getLastBuildForProject(@PathVariable("projectId") final Long projectId) {
        return buildService.getLastBuildByProjectId(projectId);
    }

    @GetMapping("id/{buildId}")
    public BuildDto getBuildById(@PathVariable("buildId") final Long buildId){
        return buildService.getBuildById(buildId);
    }

    @DeleteMapping("{projectId}/{buildId}")
    public Long deleteBuildForProject(@PathVariable("projectId") final Long projectId,
                                       @PathVariable("buildId") final Long buildId) {
        return buildService.deleteBuildByProjectId(projectId, buildId);
    }


}
