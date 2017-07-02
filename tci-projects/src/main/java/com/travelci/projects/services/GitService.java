package com.travelci.projects.services;

import com.travelci.projects.entities.PayLoad;
import com.travelci.projects.entities.ProjectDto;
import org.eclipse.jgit.api.Git;

import java.io.File;

interface GitService {

    Git pullProjectRepository(ProjectDto projectDto, PayLoad webHookPayLoad);

    boolean deleteRepository(Git repository, File repositoryFolder);

    String formatRepositoryFolderName(String projectName, String branchName);
}
