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

    @Before
    public void setUp() {
        gitService = new GitServiceImpl(ROOT_REPOSITORIES_LOCATION);
    }

    @Test
    public void shouldCreateRepository() {
        final ProjectDto projectDto = ProjectDto.builder()
            .name("Popoll Project")
            .repositoryUrl(TEST_REPOSITORY)
            .branches(Arrays.asList("master", "dev"))
            .build();

        final PayLoad payLoad = PayLoad.builder()
            .repositoryUrl(TEST_REPOSITORY)
            .branchName("dev")
            .build();

        final String attemptFolderName = "Popoll_Project_dev";

        gitService.pullProjectRepository(projectDto, payLoad);

        final File repositoryFolder = new File(ROOT_REPOSITORIES_LOCATION + attemptFolderName);
        assertThat(repositoryFolder.exists()).isTrue();
        assertThat(repositoryFolder.isDirectory()).isTrue();
        assertThat(repositoryFolder.list().length).isNotZero();
        assertThat(repositoryFolder.list()).contains(".git", ".gitignore");

        gitService.deleteRepository(repositoryFolder);
    }
}
