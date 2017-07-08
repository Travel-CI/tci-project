package com.travelci.docker.runner.docker.exceptions;

public class DockerStopContainerException extends DockerRunnerException {

    public DockerStopContainerException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
