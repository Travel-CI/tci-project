package com.travelci.projects.project;

import com.travelci.projects.webhook.entities.PayLoad;
import com.travelci.projects.project.entities.ProjectDto;

import java.util.List;

interface ProjectService {

    List<ProjectDto> getProjectsList();

    ProjectDto create(ProjectDto project);

    ProjectDto update(ProjectDto project);

    void delete(ProjectDto project);

    ProjectDto getProjectById(Long projectId);

    void manualStartProjectEngine(Long projectId, String branchName);

    void startProjectEngine(PayLoad webHookPayLoad);
}
