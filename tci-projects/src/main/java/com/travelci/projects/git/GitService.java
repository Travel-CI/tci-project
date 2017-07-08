package com.travelci.projects.git;

import com.travelci.projects.webhook.entities.PayLoad;
import com.travelci.projects.project.entities.ProjectDto;
import org.eclipse.jgit.api.Git;

import java.io.File;

public interface GitService {

    Git pullProjectRepository(ProjectDto projectDto, PayLoad webHookPayLoad);

    boolean deleteRepository(Git repository, File repositoryFolder);

    String formatRepositoryFolderName(String projectName, String branchName);
}
