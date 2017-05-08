package com.travelci.projects.services;

import com.travelci.projects.entities.PayLoad;
import com.travelci.projects.entities.ProjectDto;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class GitServiceIT {

    private GitService gitService;

    private static final String ROOT_REPOSITORIES_LOCATION = "/tmp/travel-ci/";

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
    public void shouldCreateRepository() {

        gitService.pullProjectRepository(projectDto, payLoad);

        final File repositoryFolder = new File(ROOT_REPOSITORIES_LOCATION + ATTEMPT_FOLDER_NAME);
        assertThat(repositoryFolder.exists()).isTrue();
        assertThat(repositoryFolder.isDirectory()).isTrue();
        assertThat(repositoryFolder.list().length).isNotZero();
        assertThat(repositoryFolder.list()).contains(".git", ".gitignore");

        gitService.deleteRepository(repositoryFolder);
    }

    @Test
    public void shouldPullRepositoryWhenRepositoryAlreadyExist() throws InterruptedException {

        gitService.pullProjectRepository(projectDto, payLoad);

        File repositoryFolder = new File(ROOT_REPOSITORIES_LOCATION + ATTEMPT_FOLDER_NAME);
        assertThat(repositoryFolder.exists()).isTrue();
        assertThat(repositoryFolder.isDirectory()).isTrue();
        assertThat(repositoryFolder.list().length).isNotZero();
        assertThat(repositoryFolder.list()).contains(".git", ".gitignore");

        gitService.pullProjectRepository(projectDto, payLoad);
        repositoryFolder = new File(ROOT_REPOSITORIES_LOCATION + ATTEMPT_FOLDER_NAME);
        assertThat(repositoryFolder.exists()).isTrue();
        assertThat(repositoryFolder.isDirectory()).isTrue();
        assertThat(repositoryFolder.list().length).isNotZero();
        assertThat(repositoryFolder.list()).contains(".git", ".gitignore");

        gitService.deleteRepository(repositoryFolder);
    }
}
