package com.travelci.docker.runner.docker.exceptions;

public class DockerBuildImageException extends DockerRunnerException {

    public DockerBuildImageException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
