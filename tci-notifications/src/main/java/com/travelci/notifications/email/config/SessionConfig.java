package com.travelci.notifications.email.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Configuration
@RefreshScope
@Profile({"default", "prod"})
class SessionConfig {

    @Bean
    public Session getSession(@Value("${info.email.host}") final String host,
                              @Value("${info.email.port}") final String port,
                              @Value("${info.email.socketFactory}") final String socketFactory,
                              @Value("${info.email.auth}") final String auth,
                              @Value("${info.email.tls}") final String tls,
                              @Value("${info.email.username}") final String username,
                              @Value("${info.email.password}") final String password) {

        final Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.socketFactory.port", port);
        props.put("mail.smtp.socketFactory.class", socketFactory);
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.starttls.enable", tls);

        return Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }
}