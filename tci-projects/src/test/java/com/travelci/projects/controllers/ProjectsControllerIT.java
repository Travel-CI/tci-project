package com.travelci.projects.controllers;

import com.jayway.restassured.RestAssured;
import com.travelci.projects.entities.ProjectDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:project-init.sql")
@Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:project-teardown.sql")
public class ProjectsControllerIT {

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
    @DirtiesContext
    public void shouldGetThreeProjectsWhenAddNewProject() {

        ProjectDto newProject = ProjectDto.builder()
            .name("Project 3")
            .description("Well formated project")
            .enable(true)
            .repositoryUrl("https://github.com/Popoll/popoll-project.git")
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
    public void shouldThrowExceptionWhenAddNewProjectWithUnformatedRepositoryUrl() {

        ProjectDto unformatedProject = ProjectDto.builder()
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

        unformatedProject.setRepositoryUrl("https://github.com/Popoll/popoll-project");

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

        unformatedProject.setRepositoryUrl("git://github.com/Popoll/popoll-project");

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
        ProjectDto newProject = ProjectDto.builder()
            .name("Project")
            .description("Well formated project")
            .enable(true)
            .repositoryUrl("https://github.com/Popoll/popoll-project.git")
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

        newProject.setRepositoryUrl("http://github.com/Popoll/popoll-project.git");

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
}
