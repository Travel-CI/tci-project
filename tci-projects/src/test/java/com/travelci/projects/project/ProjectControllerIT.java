package com.travelci.projects.project;

import com.jayway.restassured.RestAssured;
import com.travelci.projects.project.config.IntegrationTestsConfig;
import com.travelci.projects.webhook.entities.PayLoad;
import com.travelci.projects.project.entities.ProjectDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.util.Arrays;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:project-init.sql")
@Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:project-teardown.sql")
public class ProjectControllerIT {

    @LocalServerPort
    private int serverPort;

    private final String PROJECTS_ENDPOINT = "/projects";

    @Before
    public void init() {
        RestAssured.port = serverPort;
    }

    @Test
    public void shouldReturnTwoProjectsWhenGetAllProjects() {

        when()
            .get(PROJECTS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(OK.value())
            .body("$", hasSize(2));
    }

    @Test
    public void shouldReturnProjectOneWhenGetProjectById() {

        when()
            .get(PROJECTS_ENDPOINT + "/{projectId}", 1)
        .then()
            .log().all()
            .statusCode(OK.value())
            .body("id", equalTo(1))
            .body("name", equalTo("Project 1"));
    }

    @Test
    public void shouldThrowExceptionWhenGetAnUnknownProjectId() {

        when()
            .get(PROJECTS_ENDPOINT + "/{projectId}", 4)
        .then()
            .log().all()
            .statusCode(NOT_FOUND.value());
    }

    @Test
    public void shouldThrowExceptionWhenProjectIdParamIsNotALongObject() {

        when()
            .get(PROJECTS_ENDPOINT + "/{projectId}", "test")
        .then()
            .log().all()
            .statusCode(BAD_REQUEST.value());
    }

    @Test
    @DirtiesContext
    public void shouldGetThreeProjectsWhenAddNewProject() {

        final ProjectDto newProject = ProjectDto.builder()
            .name("Project 3")
            .description("Well formated project")
            .enable(true)
            .repositoryUrl("https://github.com/Popoll/popoll-project2.git")
            .branches(Arrays.asList("master", "dev"))
            .build();

        given()
            .contentType(JSON)
            .body(newProject)
        .when()
            .post(PROJECTS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(CREATED.value())
            .body("id", equalTo(3))
            .body("branches", contains("master", "dev"));

        when()
            .get(PROJECTS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(OK.value())
            .body("$", hasSize(3));
    }

    @Test
    @DirtiesContext
    public void shouldThrowExceptionWhenAddNewProjectWithUnformattedRepositoryUrl() {

        final ProjectDto unformatedProject = ProjectDto.builder()
            .name("Project 3")
            .description("Unformated project")
            .enable(true)
            .repositoryUrl("github.com/Popoll/popoll-project.git")
            .branches(Arrays.asList("master", "dev"))
            .build();

        given()
            .contentType(JSON)
            .body(unformatedProject)
        .when()
            .post(PROJECTS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(BAD_REQUEST.value());

        unformatedProject.setRepositoryUrl("https//github.com/Popoll/popoll-project.git");

        given()
            .contentType(JSON)
            .body(unformatedProject)
        .when()
            .post(PROJECTS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(BAD_REQUEST.value());
    }

    @Test
    @DirtiesContext
    public void shouldAddNewProjectsWhenRepositoryUrlHasGoodFormat() {

        final ProjectDto newProject = ProjectDto.builder()
            .name("Project")
            .description("Well formated project")
            .enable(true)
            .repositoryUrl("https://github.com/Popoll/popoll-project2.git")
            .branches(Arrays.asList("master", "dev"))
            .build();

        given()
            .contentType(JSON)
            .body(newProject)
        .when()
            .post(PROJECTS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(CREATED.value())
            .body("id", equalTo(3));

        newProject.setRepositoryUrl("http://github.com/Popoll/popoll-project3.git");

        given()
            .contentType(JSON)
            .body(newProject)
        .when()
            .post(PROJECTS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(CREATED.value())
            .body("id", equalTo(4));

        newProject.setRepositoryUrl("git@github.com:user/Popoll/popoll-project.git");

        given()
            .contentType(JSON)
            .body(newProject)
        .when()
            .post(PROJECTS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(CREATED.value())
            .body("id", equalTo(5));

        newProject.setRepositoryUrl("https://user@bitbucket.com/Popoll/popoll-project.git");

        given()
            .contentType(JSON)
            .body(newProject)
        .when()
            .post(PROJECTS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(CREATED.value())
            .body("id", equalTo(6));
    }

    @Test
    @DirtiesContext
    public void shouldUpdateProjectIdOneWhenPutRequest() {

        final ProjectDto updatedProject = ProjectDto.builder()
            .id(1L)
            .name("Project 1 Updated")
            .enable(false)
            .repositoryUrl("https://github.com/repo.git")
            .branches(Arrays.asList("master", "dev"))
            .build();

        given()
            .contentType(JSON)
            .body(updatedProject)
        .when()
            .put(PROJECTS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(OK.value())
            .body("id", equalTo(1))
            .body("name", equalTo(updatedProject.getName()))
            .body("repositoryUrl", equalTo(updatedProject.getRepositoryUrl()));

        when()
            .get(PROJECTS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(OK.value())
            .body("$", hasSize(2));
    }

    @Test
    @DirtiesContext
    public void shouldThrowExceptionWhenUpdateAnUnknownProject() {

        final ProjectDto updatedProject = ProjectDto.builder()
            .id(3L)
            .name("Project 3 Updated")
            .enable(false)
            .repositoryUrl("https://github.com/repo.git")
            .branches(Arrays.asList("master", "dev"))
            .build();

        given()
            .contentType(JSON)
            .body(updatedProject)
        .when()
            .put(PROJECTS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(NOT_FOUND.value());
    }

    @Test
    @DirtiesContext
    public void shouldThrowExceptionWhenUpdateProjectWithUnFormatProject() {

        ProjectDto updatedProject = ProjectDto.builder()
            .id(1L)
            .name("")
            .enable(true)
            .repositoryUrl("https://github.com/repo.git")
            .branches(Arrays.asList("master", "dev"))
            .build();

        given()
            .contentType(JSON)
            .body(updatedProject)
        .when()
            .put(PROJECTS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(BAD_REQUEST.value());

        updatedProject = ProjectDto.builder()
            .id(1L)
            .name("Project 1 updated")
            .repositoryUrl("https://github.com/repo.git")
            .branches(Arrays.asList("master", "dev"))
            .build();

        given()
            .contentType(JSON)
            .body(updatedProject)
        .when()
            .put(PROJECTS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(BAD_REQUEST.value());
    }

    @Test
    @DirtiesContext
    public void shouldGetOneProjectWhenDeleteProjectIdTwo() {

        final ProjectDto deletedProject = ProjectDto.builder()
            .id(2L)
            .name("Project 2")
            .description("Desc 2")
            .enable(true)
            .repositoryUrl("https://github.com/RC2S/RC2S-Project.git")
            .branches(Arrays.asList("master", "dev"))
            .build();

        given()
            .contentType(JSON)
            .body(deletedProject)
        .when()
            .delete(PROJECTS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(OK.value());

        when()
            .get(PROJECTS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(OK.value())
            .body("$", hasSize(1));
    }

    @Test
    @DirtiesContext
    public void shouldThrowExceptionWhenDeleteAnUnformattedProject() {

        final ProjectDto deletedProject = ProjectDto.builder()
            .id(3L)
            .repositoryUrl("https://github.com/repo.git")
            .branches(Arrays.asList("master", "dev"))
            .build();

        given()
            .contentType(JSON)
            .body(deletedProject)
        .when()
            .delete(PROJECTS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(BAD_REQUEST.value());
    }

    @Test
    @DirtiesContext
    public void shouldThrowExceptionWhenDeleteAnUnknownProject() {

        final ProjectDto deletedProject = ProjectDto.builder()
            .id(3L)
            .name("Project 3")
            .enable(true)
            .repositoryUrl("https://github.com/repo.git")
            .branches(Arrays.asList("master", "dev"))
            .build();

        given()
            .contentType(JSON)
            .body(deletedProject)
        .when()
            .delete(PROJECTS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(NOT_FOUND.value());
    }

    @Test
    public void shouldAcceptPayLoadCloneRepositoryAndSendToCommandsService() {

        final PayLoad payLoad = PayLoad.builder()
            .repositoryUrl("https://github.com/Popoll/popoll-project.git")
            .branchName("master")
            .commitAuthor("mboisnard")
            .commitHash("aaa")
            .commitMessage("Popoll commit")
            .build();

        final String ATTEMPT_FOLDER_NAME = "Project_1_master";

        given()
            .contentType(JSON)
            .body(payLoad)
        .when()
            .post(PROJECTS_ENDPOINT + "/webhook")
        .then()
            .log().all()
            .statusCode(ACCEPTED.value());

        // Assert Basic Files are present
        final File repositoryFolder = new File(IntegrationTestsConfig.ROOT_GIT_REPOSITORIES_FOLDER + ATTEMPT_FOLDER_NAME);
        assertThat(repositoryFolder.exists()).isTrue();
        assertThat(repositoryFolder.isDirectory()).isTrue();
        assertThat(repositoryFolder.list().length).isNotZero();
        assertThat(repositoryFolder.list()).contains(".git", ".gitignore");

        FileSystemUtils.deleteRecursively(new File(IntegrationTestsConfig.ROOT_GIT_REPOSITORIES_FOLDER));
    }

    @Test
    public void shouldThrowExceptionWhenPayLoadHasUnknownRepositoryUrl() {

        final PayLoad wrongRepositoryPayLoad = PayLoad.builder()
            .repositoryUrl("https://fakeurl.com/fake.git")
            .branchName("master")
            .commitAuthor("mboisnard")
            .commitHash("aaa")
            .commitMessage("Popoll commit")
            .build();

        given()
            .contentType(JSON)
            .body(wrongRepositoryPayLoad)
        .when()
            .post(PROJECTS_ENDPOINT + "/webhook")
        .then()
            .log().all()
            .statusCode(NOT_FOUND.value());
    }

    @Test
    public void shouldThrowExceptionWhenPayLoadHasWrongFormat() {

        PayLoad wrongFormatPayLoad = PayLoad.builder()
            .branchName("master")
            .commitAuthor("mboisnard")
            .commitHash("aaa")
            .commitMessage("Popoll commit")
            .build();

        given()
            .contentType(JSON)
            .body(wrongFormatPayLoad)
        .when()
            .post(PROJECTS_ENDPOINT + "/webhook")
        .then()
            .log().all()
            .statusCode(BAD_REQUEST.value());

        wrongFormatPayLoad = PayLoad.builder()
            .repositoryUrl("https://fakeurl.com/fake.git")
            .commitAuthor("mboisnard")
            .commitHash("aaa")
            .commitMessage("Popoll commit")
            .build();

        given()
            .contentType(JSON)
            .body(wrongFormatPayLoad)
        .when()
            .post(PROJECTS_ENDPOINT + "/webhook")
        .then()
            .log().all()
            .statusCode(BAD_REQUEST.value());

        wrongFormatPayLoad = PayLoad.builder()
            .repositoryUrl("https://fakeurl.com/fake.git")
            .branchName("master")
            .commitHash("aaa")
            .commitMessage("Popoll commit")
            .build();

        given()
            .contentType(JSON)
            .body(wrongFormatPayLoad)
        .when()
            .post(PROJECTS_ENDPOINT + "/webhook")
        .then()
            .log().all()
            .statusCode(BAD_REQUEST.value());

        wrongFormatPayLoad = PayLoad.builder()
            .repositoryUrl("https://fakeurl.com/fake.git")
            .branchName("master")
            .commitAuthor("mboisnard")
            .build();

        given()
            .contentType(JSON)
            .body(wrongFormatPayLoad)
        .when()
            .post(PROJECTS_ENDPOINT + "/webhook")
        .then()
            .log().all()
            .statusCode(BAD_REQUEST.value());
    }
}
