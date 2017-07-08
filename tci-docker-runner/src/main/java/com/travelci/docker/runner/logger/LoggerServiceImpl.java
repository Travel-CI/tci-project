package com.travelci.docker.runner.logger;

import com.travelci.docker.runner.command.entities.CommandDto;
import com.travelci.docker.runner.logger.entities.BuildDto;
import com.travelci.docker.runner.logger.entities.StepDto;
import com.travelci.docker.runner.logger.exceptions.LoggerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;

import static org.springframework.http.HttpStatus.OK;

@Service
@RefreshScope
@Slf4j
public class LoggerServiceImpl implements LoggerService {

    private final RestTemplate restTemplate;
    private final String loggerServiceUrl;

    public LoggerServiceImpl(final RestTemplate restTemplate,
                             @Value("{info.services.logger}") final String loggerServiceUrl) {
        this.restTemplate = restTemplate;
        this.loggerServiceUrl = loggerServiceUrl;
    }

    @Override
    public StepDto startNewStep(final CommandDto command, final BuildDto build) {

        final StepDto step = StepDto.builder()
            .stepStart(new Timestamp(System.currentTimeMillis()))
            .command(command.getCommand())
            .buildRoot(build)
            .build();

        try {
            final ResponseEntity<StepDto> response = restTemplate.postForEntity(
                loggerServiceUrl + "/steps", step, StepDto.class);

            if (!OK.equals(response.getStatusCode()))
                throw new RestClientException(
                    "Response Status Code is wrong. Expected : OK, Given : " + response.getStatusCode());

            return response.getBody();
        } catch(final RestClientException e) {
            log.error(e.getLocalizedMessage(), e.getCause());
            throw new LoggerException("Error while step creation");
        }
    }

    @Override
    public StepDto endStepByError(final StepDto step, String commandOutput) {

        step.setStepEnd(new Timestamp(System.currentTimeMillis()));
        step.setCommandResult(commandOutput);

        try {
            final ResponseEntity<StepDto> response = restTemplate.postForEntity(
                loggerServiceUrl + "/steps/error", step, StepDto.class);

            if (!OK.equals(response.getStatusCode()))
                throw new RestClientException(
                    "Response Status Code is wrong. Expected : OK, Given : " + response.getStatusCode());

            return response.getBody();
        } catch(final RestClientException e) {
            log.error(e.getLocalizedMessage(), e.getCause());
            throw new LoggerException("Error while sending step error");
        }
    }

    @Override
    public StepDto endStepBySuccess(final StepDto step, String commandOutput) {

        step.setStepEnd(new Timestamp(System.currentTimeMillis()));
        step.setCommandResult(commandOutput);

        try {
            final ResponseEntity<StepDto> response = restTemplate.postForEntity(
                loggerServiceUrl + "/steps/success", step, StepDto.class);

            if (!OK.equals(response.getStatusCode()))
                throw new RestClientException(
                    "Response Status Code is wrong. Expected : OK, Given : " + response.getStatusCode());

            return response.getBody();
        } catch(final RestClientException e) {
            log.error(e.getLocalizedMessage(), e.getCause());
            throw new LoggerException("Error while sending step success");
        }
    }

    @Override
    public BuildDto endBuildByError(final BuildDto build, final String error) {
        return null;
    }

    @Override
    public BuildDto endBuildBySuccess(final BuildDto build) {
        return null;
    }
}
