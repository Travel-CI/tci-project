package com.travelci.docker.runner.docker.exceptions;

public class DockerExecuteCommandException extends DockerRunnerException {

    public DockerExecuteCommandException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
