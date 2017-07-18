package com.travelci.notifications.email;


interface EmailService {

    void sendEmail(String sendTo, String subject, String msg);
}
