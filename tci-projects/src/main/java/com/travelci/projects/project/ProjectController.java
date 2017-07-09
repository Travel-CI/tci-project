package com.travelci.projects.project;

import com.travelci.projects.webhook.entities.PayLoad;
import com.travelci.projects.project.entities.ProjectDto;
import com.travelci.projects.project.exceptions.InvalidProjectException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(final ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public List<ProjectDto> getProjectsList() {
        return projectService.getProjectsList();
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public ProjectDto createNewProject(@Valid @RequestBody final ProjectDto project,
                                       final BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            throw new InvalidProjectException();

        return projectService.create(project);
    }

    @PutMapping
    public ProjectDto updateProject(@Valid @RequestBody final ProjectDto project,
                                    final BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            throw new InvalidProjectException();

        return projectService.update(project);
    }

    @DeleteMapping
    public void deleteProject(@Valid @RequestBody final ProjectDto project,
                              final BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            throw new InvalidProjectException();

        projectService.delete(project);
    }

    @GetMapping("{projectId}")
    public ProjectDto getProjectById(@PathVariable final Long projectId) {
        return projectService.getProjectById(projectId);
    }

    @GetMapping("/start/{projectId}/{branchName}")
    public void manualStartEngine(@PathVariable final Long projectId,
                                  @PathVariable final String branchName) {
        projectService.manualStartProjectEngine(projectId, branchName);
    }

    @PostMapping("webhook")
    @ResponseStatus(ACCEPTED)
    public void checkPayLoadAndStartEngine(@Valid @RequestBody final PayLoad webHookPayLoad,
                                           final BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            throw new InvalidProjectException();

        projectService.startProjectEngine(webHookPayLoad);
    }
}
