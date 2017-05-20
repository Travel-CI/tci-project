package com.travelci.projects.controllers;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

@Configuration
@Profile("test")
public class IntegrationTestsConfig {

    public static String ROOT_GIT_REPOSITORIES_FOLDER;

    @Bean
    public RestTemplate restTemplate() {
        return Mockito.mock(RestTemplate.class);
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

