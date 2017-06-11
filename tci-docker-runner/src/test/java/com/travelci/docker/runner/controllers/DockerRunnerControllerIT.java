package com.travelci.docker.runner.controllers;


import com.jayway.restassured.RestAssured;
import com.travelci.docker.runner.entities.CommandDto;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.springframework.http.HttpStatus.ACCEPTED;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DockerRunnerControllerIT {

    @LocalServerPort
    private int serverPort;

    private final String DOCKER_RUNNER_ENDPOINT = "/docker";

    @Before
    public void init() {
        RestAssured.port = serverPort;
    }

    @Test
    @Ignore
    public void shouldExecuteCommand() {

        given()
            .contentType(JSON)
            .body(new ArrayList<CommandDto>())
        .when()
            .post(DOCKER_RUNNER_ENDPOINT + "/execute")
        .then()
            .log().all()
            .statusCode(ACCEPTED.value());
    }
}