package com.travelci.projects.git.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class GitException extends RuntimeException {

    public GitException() {
        super();
    }

    public GitException(final String message) {
        super(message);
    }

    public GitException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
