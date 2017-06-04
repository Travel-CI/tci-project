package com.travelci.docker.runner.services;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("default")
public class DockerClientConfig {

    @Value("${info.docker.uri}")
    private String dockerClientUri;

    @Bean
    public DockerClient dockerClientBuilder() {
        return new DefaultDockerClient(dockerClientUri);
    }
}
