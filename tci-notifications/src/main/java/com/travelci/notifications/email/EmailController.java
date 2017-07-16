package com.travelci.notifications.email;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Julien on 16/07/2017.
 */
@RestController
@RequestMapping("/email")
public class EmailController {
    private final EmailService emailService;

    public EmailController(final EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendSuccessEmail(){
        emailService.sendSuccessEmail();
    }

    public void sendErrorEmail(){
        emailService.sendErrorEmail();
    }
}
