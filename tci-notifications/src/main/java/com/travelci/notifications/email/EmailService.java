package com.travelci.notifications.email;

/**
 * Created by Julien on 12/07/2017.
 */
public interface EmailService {
    void sendMessage(String to, String subject, String text);
}
