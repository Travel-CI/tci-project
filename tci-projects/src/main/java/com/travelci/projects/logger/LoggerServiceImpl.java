package com.travelci.projects.logger;

import com.travelci.projects.logger.entities.BuildDto;
import com.travelci.projects.logger.exceptions.LoggerException;
import com.travelci.projects.project.entities.ProjectDto;
import com.travelci.projects.webhook.entities.PayLoad;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;

import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Service
@RefreshScope
@Slf4j
public class LoggerServiceImpl implements LoggerService {

    private final RestTemplate restTemplate;

    private final String loggerServiceUrl;

    public LoggerServiceImpl(final RestTemplate restTemplate,
                             @Value("${info.services.logger}")
                             final String loggerServiceUrl) {
        this.restTemplate = restTemplate;
        this.loggerServiceUrl = loggerServiceUrl;
    }

    @Override
    public BuildDto startNewBuild(final PayLoad payload, final ProjectDto project) {

        final BuildDto build = BuildDto.builder()
            .buildStart(new Timestamp(System.currentTimeMillis()))
            .commitHash(payload.getCommitHash())
            .commitMessage(payload.getCommitMessage())
            .startBy(payload.getCommitAuthor())
            .branch(payload.getBranchName())
            .projectId(project.getId())
            .build();

        return sendBuildToLogger(build, "/builds", CREATED, false, "Error while build creation");
    }

    @Override
    public BuildDto endBuildByError(final BuildDto build, final Exception exception) {

        build.setError(exception.getLocalizedMessage());
        build.setBuildEnd(new Timestamp(System.currentTimeMillis()));
        return sendBuildToLogger(build, "/builds/error", OK, true, "Error while sending build error");
    }

    @SuppressWarnings("all")
    private BuildDto sendBuildToLogger(final BuildDto buildToSend, final String url,
                                       final HttpStatus expectedStatus,
                                       final Boolean isPut, final String errorMessage) {

        try {
            ResponseEntity<BuildDto> response = null;
            if (isPut) {
                final HttpEntity<BuildDto> entity = new HttpEntity<>(buildToSend);
                response = restTemplate.exchange(
                    loggerServiceUrl + url, PUT, entity, BuildDto.class);
            }
            else {
                response = restTemplate.postForEntity(
                    loggerServiceUrl + url, buildToSend, BuildDto.class);
            }

            if (!expectedStatus.equals(response.getStatusCode()))
                throw new RestClientException(
                    "Response Status Code is wrong. Expected : OK, Given : " + response.getStatusCode());

            return response.getBody();
        } catch (final RestClientException e) {
            log.error(e.getLocalizedMessage(), e.getCause());
            throw new LoggerException(errorMessage);
        }
    }
}
