package com.travelci.webhook.services.json.extractor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;

@Configuration
public class ValidatorConfig {

    @Bean
    Validator getValidator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }
}
