package com.travelci.projects.services;

import com.travelci.projects.entities.ProjectDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ProjectsServiceImpl implements ProjectsService {

    @Override
    public List<ProjectDto> getProjectsList() {
        return null;
    }

    @Override
    @Transactional
    public void create(final ProjectDto projectDto) {

    }

    @Override
    @Transactional
    public ProjectDto update(final ProjectDto projectDto) {
        return null;
    }

    @Override
    @Transactional
    public void delete(final ProjectDto projectDto) {

    }

    @Override
    public ProjectDto getProjectDetails(Integer projectId) {
        return null;
    }
}
