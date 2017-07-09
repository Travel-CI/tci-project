package com.travelci.logger.build.entities;

import org.springframework.stereotype.Component;

@Component
public class BuildAdapter {

    public BuildDto toBuildDto(final Build build) {
        return build != null ?
            BuildDto.builder()
                .id(build.getId())
                .projectId(build.getProjectId())
                .buildStart(build.getBuildStart())
                .buildEnd(build.getBuildEnd())
                .startBy(build.getStartBy())
                .commitHash(build.getCommitHash())
                .commitMessage(build.getCommitMessage())
                .branch(build.getBranch())
                .error(build.getError())
                .status(build.getStatus())
                .build()
            : null;
    }

    public Build toBuild(final BuildDto buildDto) {
        return buildDto != null ?
            Build.builder()
                .id(buildDto.getId())
                .projectId(buildDto.getProjectId())
                .buildStart(buildDto.getBuildStart())
                .buildEnd(buildDto.getBuildEnd())
                .startBy(buildDto.getStartBy())
                .commitHash(buildDto.getCommitHash())
                .commitMessage(buildDto.getCommitMessage())
                .branch(buildDto.getBranch())
                .error(buildDto.getError())
                .status(buildDto.getStatus())
                .build()
            : null;
    }
}
