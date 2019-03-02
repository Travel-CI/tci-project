package com.travelci.webhook.payload;

import io.restassured.RestAssured;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class WebhookControllerIT {

    @LocalServerPort
    private int serverPort;

    private final String WEBHOOK_ENDPOINT = "/webhook";

    @BeforeEach
    void init() {
        RestAssured.port = serverPort;
    }

    @Test
    void shouldAcceptBitbucketWebhookPayLoad() throws IOException {

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
    void shouldAcceptGithubWebhookPayLoad() throws IOException {

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
