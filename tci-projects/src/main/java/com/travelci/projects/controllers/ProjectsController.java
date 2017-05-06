package com.travelci.projects.controllers;

import com.travelci.projects.entities.ProjectDto;
import com.travelci.projects.exceptions.InvalidProjectException;
import com.travelci.projects.services.ProjectsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/projects")
public class ProjectsController {

    private final ProjectsService projectsService;

    public ProjectsController(final ProjectsService projectsService) {
        this.projectsService = projectsService;
    }

    @GetMapping
    public List<ProjectDto> getProjectsList() {
        return projectsService.getProjectsList();
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public ProjectDto createNewProject(@Valid @RequestBody final ProjectDto projectDto,
                                       final BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            throw new InvalidProjectException();

        return projectsService.create(projectDto);
    }

    @PutMapping
    public ProjectDto updateProject(@Valid @RequestBody final ProjectDto projectDto,
                                    final BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            throw new InvalidProjectException();

        return projectsService.update(projectDto);
    }

    @DeleteMapping
    public void deleteProject(@Valid @RequestBody final ProjectDto projectDto,
                              final BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            throw new InvalidProjectException();

        projectsService.delete(projectDto);
    }

    @GetMapping("{projectId}")
    public ProjectDto getProjectDetails(@PathVariable final Long projectId) {
        return projectsService.getProjectDetails(projectId);
    }

    @GetMapping("webhook")
    public void checkPayLoadForStartEngine() {

    }
}
