package com.travelci.projects.entities;

import org.springframework.stereotype.Component;

@Component
public class ProjectAdapter {

    public ProjectDto toProjectDto(final Project project) {
        return project != null ?
            ProjectDto.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .enable(project.getEnable())
                .repositoryUrl(project.getRepositoryUrl())
                .branches(project.getBranches())
                .userName(project.getUserName())
                .userPassword(project.getUserPassword())
                .repositoryToken(project.getRepositoryToken())
                .dockerfileLocation(project.getDockerfileLocation())
                .lastStart(project.getLastStart())
                .created(project.getCreated())
                .updated(project.getUpdated())
                .build()
            : null;
    }

    public Project toProjectEntity(final ProjectDto projectDto) {
        return projectDto != null ?
            Project.builder()
                .id(projectDto.getId())
                .name(projectDto.getName())
                .description(projectDto.getDescription())
                .enable(projectDto.getEnable())
                .repositoryUrl(projectDto.getRepositoryUrl())
                .branches(projectDto.getBranches())
                .userName(projectDto.getUserName())
                .userPassword(projectDto.getUserPassword())
                .repositoryToken(projectDto.getRepositoryToken())
                .dockerfileLocation(projectDto.getDockerfileLocation())
                .lastStart(projectDto.getLastStart())
                .created(projectDto.getCreated())
                .updated(projectDto.getUpdated())
                .build()
            : null;
    }
}
