package com.travelci.commands.controllers;

import com.jayway.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommandsControllerIT {

    @LocalServerPort
    private int serverPort;

    private final String COMMANDS_ENDPOINT = "/commands";

    @Before
    public void init() {
        RestAssured.port = serverPort;
    }

    @Test
    public void shouldGetCommandsList() {

    }
}