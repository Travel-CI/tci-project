package com.travelci.webhook.controllers;

import com.travelci.webhook.services.WebhookService;
import com.travelci.webhook.entities.PayLoad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private final WebhookService webhookService;

    @Autowired
    public WebhookController(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @PostMapping
    public void getWebhookPayLoad(@RequestBody PayLoad payLoad) {
        System.out.println(payLoad);
    }
}