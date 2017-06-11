package com.travelci.docker.runner.services.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class UnixCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        final String osName = context.getEnvironment().getProperty("os.name");
        return osName.contains("Linux") || osName.contains("Mac");
    }
}
