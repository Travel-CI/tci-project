package com.travelci.projects.project.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class InvalidProjectException extends RuntimeException {

    public InvalidProjectException() {
        super();
    }

    public InvalidProjectException(final String message) {
        super(message);
    }
}
