package com.travelci.docker.runner.logger;

import com.travelci.docker.runner.command.entities.CommandDto;
import com.travelci.docker.runner.logger.entities.BuildDto;
import com.travelci.docker.runner.logger.entities.StepDto;
import com.travelci.docker.runner.logger.exceptions.LoggerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
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
    public StepDto startNewStep(final CommandDto command, final BuildDto build) {

        final StepDto step = StepDto.builder()
            .stepStart(new Timestamp(System.currentTimeMillis()))
            .command(command.getCommand())
            .buildId(build.getId())
            .build();

        try {
            final ResponseEntity<StepDto> response = restTemplate.postForEntity(
                loggerServiceUrl + "/steps", step, StepDto.class);

            if (!CREATED.equals(response.getStatusCode()))
                throw new RestClientException(
                    "Response Status Code is wrong. Expected : CREATED, Given : " + response.getStatusCode());

            return response.getBody();
        } catch (final RestClientException e) {
            log.error(e.getMessage(), e.getCause());
            throw new LoggerException("Error while step creation");
        }
    }

    @Override
    public StepDto endStepByError(final StepDto step, final String commandOutput) {

        step.setStepEnd(new Timestamp(System.currentTimeMillis()));
        step.setCommandResult(commandOutput);
        return sendStepToLogger(step, "/steps/error", "Error while sending step error");
    }

    @Override
    public StepDto endStepBySuccess(final StepDto step, final String commandOutput) {

        step.setStepEnd(new Timestamp(System.currentTimeMillis()));
        step.setCommandResult(commandOutput);
        return sendStepToLogger(step, "/steps/success", "Error while sending step success");
    }

    @Override
    public BuildDto endBuildByError(final BuildDto build, final String error) {

        build.setError(error);
        build.setBuildEnd(new Timestamp(System.currentTimeMillis()));
        return sendBuildToLogger(build, "/builds/error", "Error while sending build error");
    }

    @Override
    public BuildDto endBuildBySuccess(final BuildDto build) {

        build.setBuildEnd(new Timestamp(System.currentTimeMillis()));
        return sendBuildToLogger(build, "/builds/success", "Error while sending build success");
    }

    private StepDto sendStepToLogger(final StepDto stepToSend, final String url,
                                     final String errorMessage) {

        try {
            final HttpEntity<StepDto> entity = new HttpEntity<>(stepToSend);
            final ResponseEntity<StepDto> response = restTemplate.exchange(
                loggerServiceUrl + url, PUT, entity, StepDto.class);

            if (!OK.equals(response.getStatusCode()))
                throw new RestClientException(
                    "Response Status Code is wrong. Expected : OK, Given : " + response.getStatusCode());

            return response.getBody();
        } catch (final RestClientException e) {
            log.error(e.getMessage(), e.getCause());
            throw new LoggerException(errorMessage);
        }
    }

    @SuppressWarnings("all")
    private BuildDto sendBuildToLogger(final BuildDto buildToSend, final String url,
                                       final String errorMessage) {

        try {
            final HttpEntity<BuildDto> entity = new HttpEntity<>(buildToSend);
            final ResponseEntity<BuildDto> response = restTemplate.exchange(
                loggerServiceUrl + url, PUT, entity, BuildDto.class);

            if (!OK.equals(response.getStatusCode()))
                throw new RestClientException(
                    "Response Status Code is wrong. Expected : OK, Given : " + response.getStatusCode());

            return response.getBody();
        } catch (final RestClientException e) {
            log.error(e.getMessage(), e.getCause());
            throw new LoggerException(errorMessage);
        }
    }
}
