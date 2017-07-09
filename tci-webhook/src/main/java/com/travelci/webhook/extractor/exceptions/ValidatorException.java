package com.travelci.webhook.extractor.exceptions;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidatorException extends ConstraintViolationException {

    public ValidatorException(final Set<? extends ConstraintViolation<?>> constraintViolations) {

        super(constraintViolations.stream()
                .map(c -> c.getPropertyPath() + " " + c.getMessage())
                .collect(Collectors.joining(","))
            , constraintViolations
        );
    }
}
