package com.travelci.commands.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class InvalidCommandException extends RuntimeException {

    public InvalidCommandException() {
        super();
    }

    public InvalidCommandException(final String message) {
        super(message);
    }
}
