package com.travelci.docker.runner.docker.exceptions;

public class DockerStartContainerException extends RuntimeException {

    public DockerStartContainerException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
