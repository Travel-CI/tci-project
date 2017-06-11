package com.travelci.docker.runner.exceptions;

public class DockerBuildImageException extends RuntimeException {

    public DockerBuildImageException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
