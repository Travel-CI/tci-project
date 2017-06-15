package com.travelci.docker.runner.exceptions;

public class DockerExecuteCommandException extends RuntimeException {

    public DockerExecuteCommandException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
