package com.travelci.docker.runner.exceptions;

public class DockerStartContainerException extends RuntimeException {

    public DockerStartContainerException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
