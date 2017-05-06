package com.travelci.webhook.controllers;

import com.travelci.webhook.services.webhook.WebhookService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private final WebhookService webhookService;

    public WebhookController(final WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @PostMapping
    public void getWebhookPayLoad(@RequestBody final String jsonPayLoad) {
        webhookService.sendPayloadToProjectsService(jsonPayLoad);
    }
}