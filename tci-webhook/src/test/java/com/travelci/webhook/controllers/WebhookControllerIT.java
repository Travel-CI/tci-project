package com.travelci.webhook.controllers;

import com.jayway.restassured.RestAssured;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebhookControllerIT {

    @LocalServerPort
    private int serverPort;

    private final String WEBHOOK_ENDPOINT = "/webhook";

    @Before
    public void init() {
        RestAssured.port = serverPort;
    }

    @Test
    public void shouldAcceptBitbucketWebhookPayLoad() throws IOException {

        final String bitbucketJsonPayLoad = IOUtils.toString(
            getClass().getClassLoader().getResourceAsStream("bitbucket_good_payload.json"),
            "UTF-8"
        );

        given()
            .contentType(JSON)
            .body(bitbucketJsonPayLoad)
        .when()
            .post(WEBHOOK_ENDPOINT)
        .then()
            .log().all()
            .statusCode(OK.value());
    }

    @Test
    public void shouldAcceptGithubWebhookPayLoad() throws IOException {

        final String githubJsonPayLoad = IOUtils.toString(
            getClass().getClassLoader().getResourceAsStream("github_good_payload.json"),
            "UTF-8"
        );

        given()
            .contentType(JSON)
            .body(githubJsonPayLoad)
        .when()
            .post(WEBHOOK_ENDPOINT)
        .then()
            .log().all()
            .statusCode(OK.value());
    }
}
