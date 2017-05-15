package com.travelci.projects.services;

import com.travelci.projects.entities.PayLoad;
import com.travelci.projects.entities.ProjectDto;
import org.eclipse.jgit.api.Git;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class GitServiceIT {

    private GitService gitService;

    // TODO Use Temporary File
    private static final String ROOT_REPOSITORIES_LOCATION =
        System.getProperty("os.name").toLowerCase().contains("win") ? "C:\\Temp\\" : "/tmp/travel-ci/";

    private static final String TEST_REPOSITORY = "https://github.com/Popoll/popoll-project.git";

    private static final String ATTEMPT_FOLDER_NAME = "Popoll_Project_dev";

    private ProjectDto projectDto;
    private PayLoad payLoad;

    @Before
    public void setUp() {

        gitService = new GitServiceImpl(ROOT_REPOSITORIES_LOCATION);
        projectDto = ProjectDto.builder()
            .name("Popoll Project")
            .repositoryUrl(TEST_REPOSITORY)
            .branches(Arrays.asList("master", "dev"))
            .build();

        payLoad = PayLoad.builder()
            .repositoryUrl(TEST_REPOSITORY)
            .branchName("dev")
            .build();
    }

    @Test
    public void shouldCreateRepository() throws IOException {

        final Git repository = gitService.pullProjectRepository(projectDto, payLoad);

        final File repositoryFolder = new File(ROOT_REPOSITORIES_LOCATION + ATTEMPT_FOLDER_NAME);
        assertThat(repositoryFolder.exists()).isTrue();
        assertThat(repositoryFolder.isDirectory()).isTrue();
        assertThat(repositoryFolder.list().length).isNotZero();
        assertThat(repositoryFolder.list()).contains(".git", ".gitignore");

        gitService.deleteRepository(repository, repositoryFolder);
    }

    @SuppressWarnings("all")
    @Test
    public void shouldPullRepositoryWhenRepositoryAlreadyExist() throws InterruptedException, IOException {

        // Clone Repository
        Git repository = gitService.pullProjectRepository(projectDto, payLoad);

        // Assert Repository Folder Exists with basic files inside
        File repositoryFolder = new File(ROOT_REPOSITORIES_LOCATION + ATTEMPT_FOLDER_NAME);
        assertThat(repositoryFolder.exists()).isTrue();
        assertThat(repositoryFolder.isDirectory()).isTrue();
        assertThat(repositoryFolder.list().length).isNotZero();
        assertThat(repositoryFolder.list()).contains(".git", ".gitignore");

        // Delete file for next check
        new File(ROOT_REPOSITORIES_LOCATION + ATTEMPT_FOLDER_NAME + "/.gitignore").delete();
        repository.close();

        // Reset And Pull Repository
        repository = gitService.pullProjectRepository(projectDto, payLoad);

        // Assert Basic Files are present
        repositoryFolder = new File(ROOT_REPOSITORIES_LOCATION + ATTEMPT_FOLDER_NAME);
        assertThat(repositoryFolder.exists()).isTrue();
        assertThat(repositoryFolder.isDirectory()).isTrue();
        assertThat(repositoryFolder.list().length).isNotZero();
        assertThat(repositoryFolder.list()).contains(".git", ".gitignore");

        gitService.deleteRepository(repository, repositoryFolder);
    }
}
