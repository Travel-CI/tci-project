package com.travelci.notifications.email;


public interface EmailService {

    void sendEmail(String sendTo, String subject, String msg);
}
