package com.travelci.projects.logger.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class LoggerException extends RuntimeException {

    public LoggerException(final String message) {
        super(message);
    }
}
