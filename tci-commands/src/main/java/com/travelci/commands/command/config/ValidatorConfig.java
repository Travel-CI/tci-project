package com.travelci.commands.command.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;

@Configuration
class ValidatorConfig {

    @Bean
    Validator getValidator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }
}
