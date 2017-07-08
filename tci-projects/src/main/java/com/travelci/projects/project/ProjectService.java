package com.travelci.projects.project;

import com.travelci.projects.webhook.entities.PayLoad;
import com.travelci.projects.project.entities.ProjectDto;

import java.util.List;

interface ProjectService {

    List<ProjectDto> getProjectsList();

    ProjectDto create(ProjectDto projectDto);

    ProjectDto update(ProjectDto projectDto);

    void delete(ProjectDto projectDto);

    ProjectDto getProjectById(Long projectId);

    void startProjectEngine(PayLoad webHookPayLoad);
}
