package com.travelci.notifications.email;


public interface EmailService {

    void sendSuccessEmail();

    void sendErrorEmail();

    void sendEmail(String sendTo, String subject, String msg);
}
