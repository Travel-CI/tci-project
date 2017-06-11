package com.travelci.docker.runner.services.config;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerCertificates;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Paths;

@Configuration
public class DockerClientConfig {

    @Value("${info.docker.windows.uri}")
    private String dockerSocketUriForWindows;

    @Value("${info.docker.windows.cert}")
    private String dockerCertForWindows;

    @Value("${info.docker.unix.uri}")
    private String dockerSocketUriForUnix;

    @Bean(name = "dockerClient")
    @Conditional(WindowsCondition.class)
    public DockerClient getDockerClientForWindows() throws DockerCertificateException {

        return DefaultDockerClient.builder()
            .uri(dockerSocketUriForWindows)
            .dockerCertificates(new DockerCertificates(Paths.get(dockerCertForWindows)))
            .build();
    }

    @Bean(name = "dockerClient")
    @Conditional(UnixCondition.class)
    public DockerClient getDockerClientForUnix() {
        return new DefaultDockerClient(dockerSocketUriForUnix);
    }
}
