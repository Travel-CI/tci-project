package com.travelci.docker.runner.docker.exceptions;

public class DockerRunnerException extends RuntimeException {

    public DockerRunnerException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
