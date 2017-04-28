package com.travelci.projects.services;

import com.travelci.projects.adapter.ProjectAdapter;
import com.travelci.projects.entities.ProjectDto;
import com.travelci.projects.entities.ProjectEntity;
import com.travelci.projects.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjectsServiceImpl implements ProjectsService {

    private final ProjectRepository projectRepository;

    private final ProjectAdapter projectAdapter;

    public ProjectsServiceImpl(final ProjectRepository projectRepository, final ProjectAdapter projectAdapter) {
        this.projectRepository = projectRepository;
        this.projectAdapter = projectAdapter;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDto> getProjectsList() {
        return projectRepository.findAll().stream().map(projectAdapter::toProjectDto).collect(Collectors.toList());
    }

    @Override
    public ProjectDto create(final ProjectDto projectDto) {
        return projectAdapter.toProjectDto(projectRepository.save(projectAdapter.toProjectEntity(projectDto)));
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
