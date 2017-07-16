package com.travelci.docker.runner.docker.config;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RefreshScope
class DockerClientConfig {

    @Value("${info.docker.uri}")
    private String dockerSocketUri;

    @Bean
    public DockerClient getDockerClient() {
        return DefaultDockerClient.builder()
            .uri(dockerSocketUri)
            .readTimeoutMillis(600000)
            .build();
    }
}
