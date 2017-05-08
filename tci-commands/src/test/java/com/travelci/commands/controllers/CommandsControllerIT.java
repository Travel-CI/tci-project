package com.travelci.commands.controllers;

import com.jayway.restassured.RestAssured;
import com.travelci.commands.entities.CommandDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:command-init.sql")
@Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:command-teardown.sql")
public class CommandsControllerIT {

    @LocalServerPort
    private int serverPort;

    private final String COMMANDS_ENDPOINT = "/commands";

    @Before
    public void init() {
        RestAssured.port = serverPort;
    }

    @Test
    public void shouldGetOrderedCommandListWhenGetWithProjectId() {

        when()
            .get(COMMANDS_ENDPOINT + "/{projectId}", 1)
        .then()
            .log().all()
            .statusCode(OK.value())
            .body("$", hasSize(3))
            .body("[0].id", equalTo(1))
            .body("[1].id", equalTo(3))
            .body("[2].id", equalTo(2))
            .body("commandOrder", hasItems(1, 2, 3));
    }

    @Test
    public void shouldReturnEmptyListWhenGetCommandsFromAnUnknownProjectId() {

        when()
            .get(COMMANDS_ENDPOINT + "/{fakeProjectId}", 5)
        .then()
            .log().all()
            .body("$", hasSize(0));
    }

    @Test
    public void shouldThrowExceptionWhenGetCommandsWithWrongFormatProjectId() {

        when()
            .get(COMMANDS_ENDPOINT + "/{stringProjectId}", "test")
        .then()
            .log().all()
            .statusCode(BAD_REQUEST.value());
    }

    @Test
    @DirtiesContext
    public void shouldAddNewCommandWhenCommandDtoHasGoodFormat() {

        final CommandDto newCommand = CommandDto.builder()
            .name("Docker build")
            .command("docker build -t test .")
            .projectId(3L)
            .commandOrder(1)
            .enabled(true)
            .enableLogs(true)
            .build();

        given()
            .contentType(JSON)
            .body(newCommand)
        .when()
            .post(COMMANDS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(CREATED.value())
            .body("id", equalTo(6))
            .body("projectId", equalTo(3));

        when()
            .get(COMMANDS_ENDPOINT + "/{projectId}", 3)
        .then()
            .log().all()
            .statusCode(OK.value())
            .body("$", hasSize(1));
    }

    @Test
    @DirtiesContext
    public void shouldThrowExceptionWhenAddCommandWithAlreadySavedCommandOrderIdForAProjectId() {
        final CommandDto alreadyExistCommand = CommandDto.builder()
            .name("Docker build")
            .command("docker build -t test .")
            .projectId(1L)
            .commandOrder(1)
            .enabled(true)
            .enableLogs(true)
            .build();

        given()
            .contentType(JSON)
            .body(alreadyExistCommand)
        .when()
            .post(COMMANDS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(BAD_REQUEST.value());
    }

    @Test
    @DirtiesContext
    public void shouldThrowExceptionWhenAddUnformattedCommand() {

        CommandDto unformattedCommand = CommandDto.builder().name("Docker build")
            .command("docker build -t test .").commandOrder(1)
            .enabled(true).enableLogs(true)
            .build();

        given()
            .contentType(JSON)
            .body(unformattedCommand)
        .when()
            .post(COMMANDS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(BAD_REQUEST.value());

        unformattedCommand = CommandDto.builder().name("Docker build")
            .command("docker build -t test .").projectId(3L)
            .enabled(true).enableLogs(true)
            .build();

        given()
            .contentType(JSON)
            .body(unformattedCommand)
        .when()
            .post(COMMANDS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(BAD_REQUEST.value());

        unformattedCommand = CommandDto.builder().name("Docker build")
            .projectId(3L).commandOrder(1)
            .enabled(true).enableLogs(true)
            .build();

        given()
            .contentType(JSON)
            .body(unformattedCommand)
        .when()
            .post(COMMANDS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(BAD_REQUEST.value());
    }

    @Test
    @DirtiesContext
    public void shouldUpdateCommandWhenPutFormattedAndExistingCommand() {
        final CommandDto updatedCommand = CommandDto.builder()
            .id(1L).name("Test Update")
            .command("docker build -t test .").projectId(1L)
            .commandOrder(1).enabled(false).enableLogs(false)
            .build();

        given()
            .contentType(JSON)
            .body(updatedCommand)
        .when()
            .put(COMMANDS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(OK.value())
            .body("name", equalTo(updatedCommand.getName()))
            .body("enabled", equalTo(false));

        when()
            .get(COMMANDS_ENDPOINT + "/{projectId}", 1)
        .then()
            .log().all()
            .statusCode(OK.value())
            .body("$", hasSize(3));
    }

    @Test
    @DirtiesContext
    public void shouldThrowExceptionWhenUpdateAnUnformattedCommand() {
        CommandDto unformattedCommand = CommandDto.builder().name("Test Update")
            .command("docker build -t test .").projectId(1L)
            .commandOrder(1).enabled(false).enableLogs(false)
            .build();

        given()
            .contentType(JSON)
            .body(unformattedCommand)
        .when()
            .put(COMMANDS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(NOT_FOUND.value());

        unformattedCommand = CommandDto.builder().id(1L)
            .command("docker build -t test .").projectId(1L)
            .commandOrder(1).enabled(false).enableLogs(false)
            .build();

        given()
            .contentType(JSON)
            .body(unformattedCommand)
        .when()
            .put(COMMANDS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(BAD_REQUEST.value());

        unformattedCommand = CommandDto.builder().id(1L)
            .name("Test Update").command("docker build -t test .")
            .commandOrder(1).enabled(false).enableLogs(false)
            .build();

        given()
            .contentType(JSON)
            .body(unformattedCommand)
        .when()
            .put(COMMANDS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(BAD_REQUEST.value());
    }

    @Test
    @DirtiesContext
    public void shouldThrowExceptionWhenUpdateAnUnknownCommand() {
        final CommandDto unknownCommand = CommandDto.builder()
            .id(6L).name("Test Update")
            .command("docker build -t test .").projectId(1L)
            .commandOrder(1).enabled(false).enableLogs(false)
            .build();

        given()
            .contentType(JSON)
            .body(unknownCommand)
        .when()
            .put(COMMANDS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(NOT_FOUND.value());
    }

    @Test
    @DirtiesContext
    public void shouldDeleteCommandWhenDeleteWithGoodFormatCommand() {
        final CommandDto deletedCommand = CommandDto.builder()
            .id(1L).name("Build Docker Image")
            .command("docker build -t test .").projectId(1L)
            .commandOrder(1).enabled(true).enableLogs(true)
            .build();

        given()
            .contentType(JSON)
            .body(deletedCommand)
        .when()
            .delete(COMMANDS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(OK.value());

        when()
            .get(COMMANDS_ENDPOINT + "/{projectId}", 1)
        .then()
            .log().all()
            .statusCode(OK.value())
            .body("$", hasSize(2));
    }

    @Test
    @DirtiesContext
    public void shouldThrowExceptionWhenDeleteAnUnknowCommand() {
        final CommandDto deletedCommand = CommandDto.builder()
            .id(6L).name("Build Docker Image")
            .command("docker build -t test .").projectId(1L)
            .commandOrder(1).enabled(true).enableLogs(true)
            .build();

        given()
            .contentType(JSON)
            .body(deletedCommand)
        .when()
            .delete(COMMANDS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(NOT_FOUND.value());
    }
}