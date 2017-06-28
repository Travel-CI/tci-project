package com.travelci.docker.runner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class DockerRunnerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DockerRunnerApplication.class, args);
    }
}
