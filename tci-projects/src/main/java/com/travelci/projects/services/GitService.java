package com.travelci.projects.services;

import com.travelci.projects.entities.PayLoad;
import com.travelci.projects.entities.ProjectDto;

import java.io.File;

interface GitService {

    void pullProjectRepository(ProjectDto projectDto, PayLoad webHookPayLoad);
    boolean deleteRepository(File repositoryFolder);
}
