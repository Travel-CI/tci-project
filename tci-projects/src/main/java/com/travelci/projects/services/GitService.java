package com.travelci.projects.services;

import com.travelci.projects.entities.PayLoad;
import com.travelci.projects.entities.ProjectDto;

interface GitService {

    void pullProjectRepository(final ProjectDto projectDto, final PayLoad webHookPayLoad);
}
