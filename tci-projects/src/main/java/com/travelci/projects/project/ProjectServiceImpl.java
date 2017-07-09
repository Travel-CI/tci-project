package com.travelci.projects.project;

import com.travelci.projects.git.GitService;
import com.travelci.projects.logger.LoggerService;
import com.travelci.projects.logger.entities.BuildDto;
import com.travelci.projects.webhook.entities.PayLoad;
import com.travelci.projects.project.entities.ProjectAdapter;
import com.travelci.projects.project.entities.ProjectDto;
import com.travelci.projects.git.exceptions.GitException;
import com.travelci.projects.project.exceptions.NotFoundProjectException;
import org.eclipse.jgit.api.Git;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.ACCEPTED;

@Service
@RefreshScope
@Transactional
class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectAdapter projectAdapter;
    private final GitService gitService;
    private final LoggerService loggerService;
    private final RestTemplate restTemplate;

    private final String commandsServiceUrl;

    ProjectServiceImpl(final ProjectRepository projectRepository,
                       final ProjectAdapter projectAdapter, final GitService gitService,
                       final LoggerService loggerService, final RestTemplate restTemplate,
                       @Value("${info.services.commands}") final String commandsServiceUrl) {
        this.projectRepository = projectRepository;
        this.projectAdapter = projectAdapter;
        this.gitService = gitService;
        this.loggerService = loggerService;
        this.restTemplate = restTemplate;
        this.commandsServiceUrl = commandsServiceUrl;
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
    @Transactional(readOnly = true)
    public ProjectDto getProjectById(final Long projectId) {
        return projectAdapter.toProjectDto(
            projectRepository.findById(projectId).orElseThrow(NotFoundProjectException::new)
        );
    }

    @Override
    public ProjectDto create(final ProjectDto projectDto) {

        projectDto.setCreated(new Timestamp(System.currentTimeMillis()));
        projectDto.setUpdated(projectDto.getCreated());

        return projectAdapter.toProjectDto(
            projectRepository.save(projectAdapter.toProject(projectDto))
        );
    }

    @Override
    public ProjectDto update(final ProjectDto projectDto) {

        if (projectRepository.findOne(projectDto.getId()) == null)
            throw new NotFoundProjectException();

        projectDto.setUpdated(new Timestamp(System.currentTimeMillis()));

        return projectAdapter.toProjectDto(
            projectRepository.save(projectAdapter.toProject(projectDto))
        );
    }

    @Override
    public void delete(final ProjectDto projectDto) {

        if (projectRepository.findOne(projectDto.getId()) == null)
            throw new NotFoundProjectException();

        projectRepository.delete(projectAdapter.toProject(projectDto));
    }

    @Override
    public Long deleteProjectById(final Long projectId) {
        final ProjectDto projectDto = getProjectById(projectId);
        return projectRepository.deleteById(projectDto.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public void startProjectEngine(final PayLoad webHookPayLoad) {

        // Check incoming webhook with project list
        final ProjectDto searchProject = projectAdapter.toProjectDto(
            projectRepository.findFromPayLoad(
                webHookPayLoad.getRepositoryUrl(),
                webHookPayLoad.getBranchName()
            ).orElseThrow(NotFoundProjectException::new)
        );

        // Save A New Build
        final BuildDto build = loggerService.startNewBuild(webHookPayLoad, searchProject);

        // Set the Last Start after new build is created
        searchProject.setLastStart(new Timestamp(System.currentTimeMillis()));
        projectRepository.save(projectAdapter.toProject(searchProject));

        // Get Source Code by calling GitService
        try {
            final Git repository = gitService.pullProjectRepository(searchProject, webHookPayLoad);
            repository.close();
        } catch(final GitException e) {
            loggerService.endBuildByError(build, e);
        }

        // Send Request to tci-commands
        try {

            // Setup Project Dto Object for Commands and Docker Runner
            final ProjectDto sentProjectDto = ProjectDto.builder()
                .id(searchProject.getId())
                .name(gitService.formatRepositoryFolderName(
                        searchProject.getName(),
                        webHookPayLoad.getBranchName())
                )
                .currentBuild(build)
                .dockerfileLocation(searchProject.getDockerfileLocation())
                .build();

            final ResponseEntity<Void> response = restTemplate.postForEntity(
                commandsServiceUrl + "/commands/project",
                sentProjectDto,
                Void.class
            );

            if (!ACCEPTED.equals(response.getStatusCode()))
                throw new RestClientException(
                    "Response Status Code is wrong. Expected : ACCEPTED, Given : " + response.getStatusCode());

        } catch (final RestClientException e) {
            loggerService.endBuildByError(build, e);
        }
    }
}
