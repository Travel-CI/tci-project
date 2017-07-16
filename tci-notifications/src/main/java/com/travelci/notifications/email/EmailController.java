package com.travelci.notifications.email;

import com.travelci.notifications.email.entities.Email;
import com.travelci.notifications.email.exceptions.InvalidEmailException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(final EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/success")
    public void sendSuccessEmail(@Valid @RequestBody final Email email,
                                 final BindingResult bindingResult){

        if(bindingResult.hasErrors())
            throw new InvalidEmailException();

        emailService.sendEmail(email.getSendTo(), email.getSubject(), email.getMessage());
    }

    @PostMapping("/error")
    public void sendErrorEmail(@Valid @RequestBody final Email email,
                               final BindingResult bindingResult){

        if(bindingResult.hasErrors())
            throw new InvalidEmailException();

        emailService.sendEmail(email.getSendTo(), email.getSubject(), email.getMessage());
    }
}
