package com.travelci.projects.project;

import com.travelci.projects.project.entities.Project;
import com.travelci.projects.webhook.entities.PayLoad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.jdbc.EmbeddedDatabaseConnection.H2;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = H2)
@Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:project-teardown.sql")
class ProjectRepositoryIT {

    @Autowired
    private ProjectRepository projectRepository;

    private Project project;
    private List<String> branchList;

    @BeforeEach
    void setUp() {

        branchList = Arrays.asList("master", "dev", "features/TCI-1");

        project = Project.builder()
            .name("Project 1")
            .enable(true)
            .repositoryUrl("https://github.com/Popoll/popoll-project.git")
            .description("Project 1 with branch list")
            .branches(branchList)
            .build();
    }

    @Test
    @DirtiesContext
    void shouldSaveProjectWithBranchListInsideStringColumn() {

        final Project savedProject = projectRepository.save(project);

        assertThat(savedProject.getId()).isEqualTo(1);
        assertThat(savedProject.getBranches()).isEqualTo(branchList);
    }

    @Test
    @DirtiesContext
    void shouldReturnProjectWhenExecuteFromPayLoadQuery() {

        final PayLoad payLoad = PayLoad.builder()
            .repositoryUrl("https://github.com/Popoll/popoll-project.git")
            .branchName("dev")
            .build();

        projectRepository.save(project);

        final Project searchProject = projectRepository.findFromPayLoad(
            payLoad.getRepositoryUrl(),
            payLoad.getBranchName()
        ).orElse(null);

        assertThat(searchProject).isNotNull();
        assertThat(searchProject.getId()).isEqualTo(1);
        assertThat(searchProject.getBranches()).isEqualTo(branchList);
    }
}
