package com.travelci.commands.logger;

import com.travelci.commands.logger.entities.BuildDto;
import com.travelci.commands.logger.exceptions.LoggerException;
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
    public BuildDto endBuildByError(final BuildDto build, final Exception exception) {

        build.setError(exception.getLocalizedMessage());
        build.setBuildEnd(new Timestamp(System.currentTimeMillis()));

        try {
            final HttpEntity<BuildDto> entity = new HttpEntity<>(build);
            final ResponseEntity<BuildDto> response = restTemplate.exchange(
                loggerServiceUrl + "/builds/error", PUT, entity, BuildDto.class);

            if (!OK.equals(response.getStatusCode()))
                throw new RestClientException(
                    "Response Status Code is wrong. Expected : OK, Given : " + response.getStatusCode());

            return response.getBody();
        } catch(final RestClientException e) {
            log.error(e.getLocalizedMessage(), e.getCause());
            throw new LoggerException("Error while sending build error");
        }
    }
}
