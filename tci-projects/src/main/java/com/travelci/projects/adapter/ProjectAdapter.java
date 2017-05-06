package com.travelci.projects.adapter;

import com.travelci.projects.entities.ProjectDto;
import com.travelci.projects.entities.ProjectEntity;
import org.springframework.stereotype.Component;

@Component
public class ProjectAdapter {

    public ProjectDto toProjectDto(final ProjectEntity project) {
        return project != null ?
            ProjectDto.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .enable(project.getEnable())
                .repositoryUrl(project.getRepositoryUrl())
                .branches(project.getBranches())
                .dockerFileLocation(project.getDockerFileLocation())
                .lastStart(project.getLastStart())
                .build()
            : null;
    }

    public ProjectEntity toProjectEntity(final ProjectDto projectDto) {
        return projectDto != null ?
            ProjectEntity.builder()
                .id(projectDto.getId())
                .name(projectDto.getName())
                .description(projectDto.getDescription())
                .enable(projectDto.getEnable())
                .repositoryUrl(projectDto.getRepositoryUrl())
                .branches(projectDto.getBranches())
                .dockerFileLocation(projectDto.getDockerFileLocation())
                .lastStart(projectDto.getLastStart())
                .created(projectDto.getCreated())
                .updated(projectDto.getUpdated())
                .build()
            : null;
    }
}
