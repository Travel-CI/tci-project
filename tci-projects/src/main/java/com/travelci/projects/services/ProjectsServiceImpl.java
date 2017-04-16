package com.travelci.projects.services;

import com.travelci.projects.entities.ProjectDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProjectsServiceImpl implements ProjectsService {

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDto> getProjectsList() {
        return null;
    }

    @Override

    public ProjectDto create(final ProjectDto projectDto) {
        return null;
    }

    @Override
    public ProjectDto update(final ProjectDto projectDto) {
        return null;
    }

    @Override
    public void delete(final ProjectDto projectDto) {

    }

    @Override
    @Transactional(readOnly = true)
    public ProjectDto getProjectDetails(final Integer projectId) {
        return null;
    }
}
