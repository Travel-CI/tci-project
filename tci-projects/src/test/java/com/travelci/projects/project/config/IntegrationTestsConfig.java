package com.travelci.projects.project.config;

import com.travelci.projects.logger.entities.BuildDto;
import com.travelci.projects.project.entities.ProjectDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.CREATED;

@Configuration
@Profile("test")
public class IntegrationTestsConfig {

    public static String ROOT_GIT_REPOSITORIES_FOLDER;

    @Bean
    public RestTemplate restTemplate() {

        final RestTemplate restTemplate = mock(RestTemplate.class);

        when(restTemplate.postForEntity(any(String.class), any(ProjectDto.class), anyObject()))
            .thenReturn(new ResponseEntity<>(ACCEPTED));

        when(restTemplate.postForEntity(eq("http://localhost:8083/builds"), any(BuildDto.class), anyObject()))
            .thenReturn(new ResponseEntity<>(BuildDto.builder().id(1L).build(), CREATED));

        return restTemplate;
    }

    @Bean
    public PropertySourcesPlaceholderConfigurer propertiesResolver() throws IOException {
        final PropertySourcesPlaceholderConfigurer pspc =  new PropertySourcesPlaceholderConfigurer();
        final Properties properties = new Properties();

        ROOT_GIT_REPOSITORIES_FOLDER = Files.createTempDirectory("travel-ci").toString() + "/";
        properties.setProperty("info.repositories.location", ROOT_GIT_REPOSITORIES_FOLDER);

        pspc.setProperties(properties);

        return pspc;
    }
}

