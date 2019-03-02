package com.travelci.commands.command.config;

import com.travelci.commands.command.entities.CommandDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.ACCEPTED;

@Configuration
@Profile("test")
class IntegrationTestsConfig {

    @Bean
    public RestTemplate restTemplate() {

        final RestTemplate restTemplate = mock(RestTemplate.class);

        when(restTemplate.postForEntity(any(String.class), any(CommandDto.class), any()))
            .thenReturn(new ResponseEntity<>(ACCEPTED));

        return restTemplate;
    }
}
