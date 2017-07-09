package com.travelci.logger.build;

import com.travelci.logger.build.entities.BuildDto;

import java.util.List;

interface BuildService {

    BuildDto create(BuildDto buildDto);

    BuildDto endBySuccess(BuildDto buildDto);

    BuildDto endByError(BuildDto buildDto);

    List<BuildDto> getBuildsByProjectId(Long projectId);

    Long deleteAllBuildsByProjectId(Long projectId);
}
