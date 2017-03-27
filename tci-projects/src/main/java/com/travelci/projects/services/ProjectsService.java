package com.travelci.projects.services;

import com.travelci.projects.entities.ProjectDto;

import java.util.List;

public interface ProjectsService {

    List<ProjectDto> getProjectsList();

    ProjectDto create(final ProjectDto projectDto);

    ProjectDto update(final ProjectDto projectDto);

    void delete(final ProjectDto projectDto);

    ProjectDto getProjectDetails(final Integer projectId);
}
