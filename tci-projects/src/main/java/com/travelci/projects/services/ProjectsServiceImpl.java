package com.travelci.projects.services;

import com.travelci.projects.adapter.ProjectAdapter;
import com.travelci.projects.entities.PayLoad;
import com.travelci.projects.entities.ProjectDto;
import com.travelci.projects.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjectsServiceImpl implements ProjectsService {

    private final ProjectRepository projectRepository;
    private final ProjectAdapter projectAdapter;
    private final GitService gitService;

    public ProjectsServiceImpl(final ProjectRepository projectRepository,
                               final ProjectAdapter projectAdapter,
                               final GitService gitService) {
        this.projectRepository = projectRepository;
        this.projectAdapter = projectAdapter;
        this.gitService = gitService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDto> getProjectsList() {
        return projectRepository.findAll()
            .stream()
            .map(projectAdapter::toProjectDto)
            .collect(Collectors.toList());
    }

    @Override
    public ProjectDto create(final ProjectDto projectDto) {

        projectDto.setCreated(new Timestamp(System.currentTimeMillis()));
        projectDto.setUpdated(projectDto.getCreated());

        return projectAdapter.toProjectDto(
            projectRepository.save(projectAdapter.toProjectEntity(projectDto))
        );
    }

    @Override
    public ProjectDto update(final ProjectDto projectDto) {

        projectDto.setUpdated(new Timestamp(System.currentTimeMillis()));

        return projectAdapter.toProjectDto(
            projectRepository.save(projectAdapter.toProjectEntity(projectDto))
        );
    }

    @Override
    public void delete(final ProjectDto projectDto) {
        projectRepository.delete(projectAdapter.toProjectEntity(projectDto));
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectDto getProjectDetails(final Long projectId) {
        return projectAdapter.toProjectDto(
            projectRepository.findOne(projectId)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public void checkPayLoadFromWebHook(final PayLoad webHookPayLoad) {

        ProjectDto searchProject = null; //projectAdapter.toProjectDto(projectRepository.); // FindByRepositoryUrlAndEnabledAndBranchName
        gitService.pullProjectRepository(searchProject, webHookPayLoad);
    }
}
