package com.travelci.projects.adapter;

import com.travelci.projects.entities.ProjectDto;
import com.travelci.projects.entities.ProjectEntity;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class ProjectAdapter {

    public ProjectDto toProjectDto(ProjectEntity project) {
        return ProjectDto.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .enable(project.getEnable())
                .repositoryUrl(project.getRepositoryUrl())
                .branches(project.getBranches())
                .dockerFileLocation(project.getDockerFileLocation())
                .lastStart(project.getLastStart())
                .build();
    }

    public ProjectEntity toProjectEntity(ProjectDto projectDto) {
        return ProjectEntity.builder()
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
                .build();
    }
}
