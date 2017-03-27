package com.travelci.projects.controllers;

import com.travelci.projects.entities.ProjectDto;
import com.travelci.projects.exceptions.InvalidProjectException;
import com.travelci.projects.services.ProjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectsController {

    private ProjectsService projectsService;

    @Autowired
    public ProjectsController(ProjectsService projectsService) {
        this.projectsService = projectsService;
    }

    @GetMapping
    public List<ProjectDto> getProjectsList() {
        return projectsService.getProjectsList();
    }

    @PostMapping
    public ProjectDto createNewProject(@Valid @RequestBody ProjectDto projectDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            throw new InvalidProjectException();

        return projectsService.create(projectDto);
    }

    @PutMapping
    public ProjectDto updateProject(@Valid @RequestBody ProjectDto projectDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            throw new InvalidProjectException();

        return projectsService.update(projectDto);
    }

    @DeleteMapping
    public void deleteProject(@Valid @RequestBody ProjectDto projectDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            throw new InvalidProjectException();

        projectsService.delete(projectDto);
    }

    @GetMapping("project_id")
    public ProjectDto getProjectDetails(@RequestParam("project_id") Integer projectId) {
        return projectsService.getProjectDetails(projectId);
    }

    @GetMapping("webhook")
    public void checkPayLoadForStartEngine() {

    }
}
