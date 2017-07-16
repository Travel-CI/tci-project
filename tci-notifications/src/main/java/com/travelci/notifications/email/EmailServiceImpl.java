package com.travelci.notifications.email;

import com.travelci.notifications.email.entities.Email;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService{

    private final Session session;

    public EmailServiceImpl(final Session session) {
        this.session = session;
    }

    @Override
    public void sendSuccessEmail(final Email email) {
        sendEmail(email.getSendTo(), email.getSubject(), email.getMessage());
    }

    @Override
    public void sendErrorEmail(final Email email) {
        sendEmail(email.getSendTo(), email.getSubject(), email.getMessage());
    }

    @Override
    public void sendEmail(final String sendTo,final String subject,final String msg){
        try {

            final Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("travelci4al@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(sendTo));
            message.setSubject(subject);
            message.setText(msg);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
