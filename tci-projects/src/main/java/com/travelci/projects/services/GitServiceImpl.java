package com.travelci.projects.services;

import com.travelci.projects.entities.PayLoad;
import com.travelci.projects.entities.ProjectDto;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;

import static java.util.Collections.singleton;

@Service
@RefreshScope
class GitServiceImpl implements GitService {

    private final String rootRepositoriesLocation;

    public GitServiceImpl(@Value("{info.repositories.location}") final String rootRepositoriesLocation) {
        this.rootRepositoriesLocation = rootRepositoriesLocation;
    }

    public void pullProjectRepository(final ProjectDto projectDto,
                                      final PayLoad webHookPayLoad) {

        final File repositoryFolder = new File(formatRepositoryFolderName(
            projectDto.getName(),
            webHookPayLoad.getBranchName()
        ));

        if (!repositoryFolder.exists() ||
            (repositoryFolder.exists() &&
            repositoryFolder.isDirectory() &&
            repositoryFolder.list().length == 0)) {

            cloneRepositoryBranch(repositoryFolder,
                projectDto.getRepositoryUrl(),
                webHookPayLoad.getBranchName()
            );
        } else if (repositoryFolder.exists() &&
            repositoryFolder.isDirectory()) {

            try {
                pullRepositoryBranch(repositoryFolder,
                    projectDto.getRepositoryUrl(),
                    webHookPayLoad.getBranchName()
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*
        http://www.codeaffine.com/2015/05/06/jgit-initialize-repository/
        http://www.codeaffine.com/2015/11/30/jgit-clone-repository/
        http://www.codeaffine.com/2014/12/09/jgit-authentication/
        Ssh -> user + password
        Https -> OAuth token
         */
    }

    private Git cloneRepositoryBranch(final File repositoryFolder,
                                      final String repositoryUrl, final String branch) {

        final String remoteBranch = "refs/heads/" + branch;

        try {
            return Git.cloneRepository()
                .setURI(repositoryUrl)
                .setDirectory(repositoryFolder)
                .setBranchesToClone(singleton(remoteBranch))
                .setBranch(remoteBranch)
                .call();

        } catch (GitAPIException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Git pullRepositoryBranch(final File repositoryFolder, final String repositoryUrl,
                                     final String branch) throws IOException {
        try {
            final Git repository = Git.open(repositoryFolder);
            repository.pull().call();
            return repository;
        } catch (GitAPIException e) {
            e.printStackTrace();

            // Delete all files in folder
            deleteRepository(repositoryFolder);

            // Clone repository
            return cloneRepositoryBranch(repositoryFolder, repositoryUrl, branch);
        }
    }

    public boolean deleteRepository(final File repositoryFolder) {
        return FileSystemUtils.deleteRecursively(repositoryFolder);
    }

    private String formatRepositoryFolderName(final String projectName, final String branchName) {
        return new StringBuilder()
            .append(rootRepositoriesLocation)
            .append(projectName.replaceAll(" ", "_"))
            .append("_")
            .append(branchName)
            .toString();
    }
}
