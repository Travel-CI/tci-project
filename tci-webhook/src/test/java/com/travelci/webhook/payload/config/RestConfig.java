package com.travelci.webhook.payload.config;

import com.travelci.webhook.payload.entities.PayLoad;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.ACCEPTED;

@Configuration
@Profile("test")
class RestConfig {

    @Bean
    public RestTemplate restTemplate() {

        final RestTemplate restTemplate = mock(RestTemplate.class);

        when(restTemplate.postForEntity(any(String.class), any(PayLoad.class), anyObject()))
            .thenReturn(new ResponseEntity<>(ACCEPTED));

        return restTemplate;
    }
}
