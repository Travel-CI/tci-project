package com.travelci.notifications.email;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Julien on 12/07/2017.
 */
@RestController
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(final EmailService emailService){
        this.emailService = emailService;
    }

    @GetMapping
    public void createEmail(){
        emailService.sendMessage("julien.bertauld@gmail.com", "toto","test");
    }
}
