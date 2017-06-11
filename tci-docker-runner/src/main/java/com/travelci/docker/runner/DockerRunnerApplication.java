package com.travelci.docker.runner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;

//@EnableDiscoveryClient
@SpringBootApplication
public class DockerRunnerApplication extends AsyncConfigurerSupport {

    public static void main(String[] args) {
        SpringApplication.run(DockerRunnerApplication.class, args);
    }
}
