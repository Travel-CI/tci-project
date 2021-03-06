package com.travelci.commands.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jayway.restassured.RestAssured;
import com.travelci.commands.command.entities.CommandDto;
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

import java.util.ArrayList;
import java.util.List;

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
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:command-init.sql")
@Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:command-teardown.sql")
public class CommandControllerIT {

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
            .command("docker build -t test .")
            .projectId(3L)
            .commandOrder(1)
            .enabled(true)
            .enableLogs(true)
            .build();

        final List<CommandDto> commandsList = new ArrayList<>();
        commandsList.add(newCommand);

        given()
            .contentType(JSON)
            .body(commandsList)
        .when()
            .post(COMMANDS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(CREATED.value())
            .body("$", hasSize(1))
            .body("[0].id", equalTo(6))
            .body("[0].projectId", equalTo(3));

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
            .command("docker build -t test .")
            .projectId(1L)
            .commandOrder(1)
            .enabled(true)
            .enableLogs(true)
            .build();

        final List<CommandDto> commandsList = new ArrayList<>();
        commandsList.add(alreadyExistCommand);

        given()
            .contentType(JSON)
            .body(commandsList)
        .when()
            .post(COMMANDS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(BAD_REQUEST.value());
    }

    @Test
    @DirtiesContext
    public void shouldThrowExceptionWhenAddUnformattedCommand() {

        final List<CommandDto> commandsList = new ArrayList<>();
        final Gson gson = new GsonBuilder().create();

        CommandDto unformattedCommand = CommandDto.builder()
            .command("docker build -t test .").commandOrder(1)
            .enabled(true).enableLogs(true)
            .build();

        commandsList.add(unformattedCommand);

        given()
            .contentType(JSON)
            .body(gson.toJson(commandsList))
        .when()
            .post(COMMANDS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(BAD_REQUEST.value());

        unformattedCommand = CommandDto.builder()
            .command("docker build -t test .").projectId(3L)
            .enabled(true).enableLogs(true)
            .build();

        commandsList.clear();
        commandsList.add(unformattedCommand);

        given()
            .contentType(JSON)
            .body(gson.toJson(commandsList))
        .when()
            .post(COMMANDS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(BAD_REQUEST.value());

        unformattedCommand = CommandDto.builder()
            .projectId(3L).commandOrder(1)
            .enabled(true).enableLogs(true)
            .build();

        commandsList.clear();
        commandsList.add(unformattedCommand);

        given()
            .contentType(JSON)
            .body(gson.toJson(commandsList))
        .when()
            .post(COMMANDS_ENDPOINT)
        .then()
            .log().all()
            .statusCode(BAD_REQUEST.value());
    }

    @Test
    @DirtiesContext
    public void shouldUpdateCommandWhenPutFormattedAndDeleteOtherCommands() {

        final List<CommandDto> commands = new ArrayList<>();

        final CommandDto updatedCommand = CommandDto.builder()
            .id(1L)
            .command("docker build -t test .").projectId(1L)
            .commandOrder(1).enabled(false).enableLogs(false)
            .build();

        commands.add(updatedCommand);
        final Gson gson = new GsonBuilder().create();
        final String jsonCommands = gson.toJson(commands);

        given()
            .contentType(JSON)
            .body(jsonCommands)
        .when()
            .put(COMMANDS_ENDPOINT + "/{projectId}", 1)
        .then()
            .log().all()
            .statusCode(OK.value())
            .body("[0].enabled", equalTo(false));

        when()
            .get(COMMANDS_ENDPOINT + "/{projectId}", 1)
        .then()
            .log().all()
            .statusCode(OK.value())
            .body("$", hasSize(1));
    }

    @Test
    @DirtiesContext
    public void shouldThrowExceptionWhenUpdateAnUnformattedCommand() {

        final List<CommandDto> commands = new ArrayList<>();

        CommandDto unformattedCommand = CommandDto.builder()
            .projectId(1L)
            .commandOrder(1).enabled(false).enableLogs(false)
            .build();

        commands.add(unformattedCommand);

        given()
            .contentType(JSON)
            .body(commands)
        .when()
            .put(COMMANDS_ENDPOINT + "/{projectId}", 1)
        .then()
            .log().all()
            .statusCode(BAD_REQUEST.value());

        unformattedCommand = CommandDto.builder().id(1L)
            .command("docker build -t test .")
            .commandOrder(1).enabled(false).enableLogs(false)
            .build();

        commands.clear();
        commands.add(unformattedCommand);

        given()
            .contentType(JSON)
            .body(unformattedCommand)
        .when()
            .put(COMMANDS_ENDPOINT + "/{projectId}", 1)
        .then()
            .log().all()
            .statusCode(BAD_REQUEST.value());

        unformattedCommand = CommandDto.builder().id(1L)
            .command("docker build -t test .")
            .commandOrder(1).enableLogs(false)
            .build();

        commands.clear();
        commands.add(unformattedCommand);

        given()
            .contentType(JSON)
            .body(unformattedCommand)
        .when()
            .put(COMMANDS_ENDPOINT + "/{projectId}", 1)
        .then()
            .log().all()
            .statusCode(BAD_REQUEST.value());
    }
}