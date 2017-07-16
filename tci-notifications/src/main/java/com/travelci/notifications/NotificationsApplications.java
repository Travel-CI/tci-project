package com.travelci.notifications;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by Julien on 12/07/2017.
 */
//@EnableDiscoveryClient
@SpringBootApplication
public class NotificationsApplications {
    public static void main(String args[]) {

        SpringApplication.run(NotificationsApplications.class, args);
        final String username = "travelci4al@gmail.com";
        final String password = "4altravelci";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("travelci4al@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("julien.bertauld@gmail.com"));
            message.setSubject("Java Mail");
            message.setText("Troll ultime ");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
