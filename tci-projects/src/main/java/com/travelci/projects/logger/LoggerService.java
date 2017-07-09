package com.travelci.projects.logger;

import com.travelci.projects.logger.entities.BuildDto;
import com.travelci.projects.project.entities.ProjectDto;

import com.travelci.projects.webhook.entities.PayLoad;


public interface LoggerService {

    BuildDto startNewBuild(PayLoad payload, ProjectDto project);

    BuildDto endBuildByError(BuildDto build, Exception exception);
}
