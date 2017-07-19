package com.travelci.projects.git;

import com.travelci.projects.webhook.entities.PayLoad;
import com.travelci.projects.project.entities.ProjectDto;
import com.travelci.projects.git.exceptions.GitException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;

import static java.util.Collections.singleton;
import static org.eclipse.jgit.api.ResetCommand.ResetType.HARD;

/*
http://www.codeaffine.com/2015/05/06/jgit-initialize-repository/
http://www.codeaffine.com/2015/11/30/jgit-clone-repository/
http://www.codeaffine.com/2014/12/09/jgit-authentication/
Ssh -> user + password
Https -> OAuth token
Async
https://spring.io/guides/gs/async-method/
http://callistaenterprise.se/blogg/teknik/2014/04/22/c10k-developing-non-blocking-rest-services-with-spring-mvc/
 */
@Service
@RefreshScope
class GitServiceImpl implements GitService {

    private final String rootRepositoriesLocation;

    GitServiceImpl(@Value("${info.repositories.location}")
                   final String rootRepositoriesLocation) {
        this.rootRepositoriesLocation = rootRepositoriesLocation;
    }

    public Git pullProjectRepository(final ProjectDto project,
                                     final PayLoad webHookPayLoad) {

        final File repositoryFolder = new File(formatRepositoryFolderName(
            project.getName(),
            webHookPayLoad.getBranchName()
        ));

        if (!repositoryFolder.exists() ||
            (repositoryFolder.exists() &&
            repositoryFolder.isDirectory() &&
            repositoryFolder.list().length == 0)) {

            return cloneRepositoryBranch(repositoryFolder,
                project.getRepositoryUrl(),
                webHookPayLoad.getBranchName()
            );
        } else if (repositoryFolder.exists() &&
            repositoryFolder.isDirectory()) {

            return pullRepositoryBranch(repositoryFolder,
                project.getRepositoryUrl(),
                webHookPayLoad.getBranchName()
            );
        }

        throw new GitException("Fail to choose Git method to get your repository.");
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

        } catch (final GitAPIException e) {
            throw new GitException("Failed to clone " + branch + " from " + repositoryUrl, e);
        }
    }

    private Git pullRepositoryBranch(final File repositoryFolder, final String repositoryUrl,
                                     final String branch) {

        Git repository = null;

        try {
            repository = Git.open(repositoryFolder);
            repository.reset().setMode(HARD).call();
            repository.pull().call();

            return repository;
        } catch (final IOException e) {
            throw new GitException("Fail to open " + repositoryFolder, e);
        } catch (final GitAPIException e) {
            // Delete all files in folder
            deleteRepository(repository, repositoryFolder);

            // Clone repository
            return cloneRepositoryBranch(repositoryFolder, repositoryUrl, branch);
        }
    }

    public boolean deleteRepository(final Git repository, final File repositoryFolder) {

        if (repository != null)
            repository.close();

        return FileSystemUtils.deleteRecursively(repositoryFolder);
    }

    public String formatRepositoryFolderName(final String projectName, final String branchName) {
        return new StringBuilder()
            .append(rootRepositoriesLocation)
            .append(projectName.replaceAll(" ", "_"))
            .append("_")
            .append(branchName)
            .toString();
    }
}
