package com.travelci.docker.runner.docker.exceptions;

public class DockerBuildImageException extends RuntimeException {

    public DockerBuildImageException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
