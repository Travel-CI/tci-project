package com.travelci.docker.runner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

//@EnableDiscoveryClient
@SpringBootApplication
@EnableAsync
public class DockerRunnerApplication extends AsyncConfigurerSupport {

    public static void main(String[] args) {
        SpringApplication.run(DockerRunnerApplication.class, args);
    }

    @Override
    public Executor getAsyncExecutor() {

        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("DockerRunnerLookup-");
        executor.initialize();

        return executor;
    }
}
