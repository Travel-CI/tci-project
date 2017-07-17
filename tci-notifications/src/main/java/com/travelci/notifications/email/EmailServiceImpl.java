package com.travelci.notifications.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
@RefreshScope
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final Session session;

    private final String emailSender;

    public EmailServiceImpl(final Session session,
                            @Value("${info.email.username}")
                            final String emailSender) {
        this.session = session;
        this.emailSender = emailSender;
    }

    @Override
    public void sendEmail(final String sendTo,final String subject,final String msg){

        try {
            final Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailSender));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(sendTo));
            message.setSubject(subject);
            message.setText(msg);

            Transport.send(message);

        } catch (final MessagingException e) {
            log.error(e.getMessage(), e.getCause());
        }
    }
}
