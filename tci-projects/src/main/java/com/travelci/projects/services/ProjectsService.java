package com.travelci.projects.services;

import com.travelci.projects.entities.PayLoad;
import com.travelci.projects.entities.ProjectDto;

import java.util.List;

public interface ProjectsService {

    List<ProjectDto> getProjectsList();

    ProjectDto create(ProjectDto projectDto);

    ProjectDto update(ProjectDto projectDto);

    void delete(ProjectDto projectDto);

    ProjectDto getProjectById(Long projectId);

    void startProjectEngine(PayLoad webHookPayLoad);
}
