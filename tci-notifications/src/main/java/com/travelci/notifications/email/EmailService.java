package com.travelci.notifications.email;


import com.travelci.notifications.email.entities.Email;

public interface EmailService {

    void sendSuccessEmail(Email email);

    void sendErrorEmail(Email email);

    void sendEmail(String sendTo, String subject, String msg);
}
