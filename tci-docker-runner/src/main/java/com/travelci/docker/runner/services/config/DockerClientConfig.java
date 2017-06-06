package com.travelci.docker.runner.services.config;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DockerClientConfig {

    @Value("${info.docker.windows.uri}")
    private String dockerSocketUriForWindows;

    @Value("${info.docker.unix.uri")
    private String dockerSocketUriForUnix;

    @Bean(name = "dockerClient")
    @Conditional(WindowsCondition.class)
    public DockerClient getDockerClientForWindows() {
        return new DefaultDockerClient(dockerSocketUriForWindows);
    }

    @Bean(name = "dockerClient")
    @Conditional(UnixCondition.class)
    public DockerClient getDockerClientForUnix() {
        return new DefaultDockerClient(dockerSocketUriForUnix);
    }
}
