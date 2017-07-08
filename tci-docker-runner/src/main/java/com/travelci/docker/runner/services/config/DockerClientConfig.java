package com.travelci.docker.runner.services.config;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DockerClientConfig {

    @Value("${info.docker.uri}")
    private String dockerSocketUri;

    @Bean
    public DockerClient getDockerClientForUnix() {
        return new DefaultDockerClient(dockerSocketUri);
    }
}
