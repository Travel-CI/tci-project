package com.travelci.webhook.extractor.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class ExtractorNotFoundException extends RuntimeException {}
