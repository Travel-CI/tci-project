package com.travelci.logger.build;

import com.travelci.logger.build.entities.BuildDto;

import java.util.List;

interface BuildService {

    BuildDto create(BuildDto build);

    BuildDto endBySuccess(BuildDto build);

    BuildDto endByError(BuildDto build);

    List<BuildDto> getBuildsByProjectId(Long projectId);

    Long deleteAllBuildsByProjectId(Long projectId);
}
