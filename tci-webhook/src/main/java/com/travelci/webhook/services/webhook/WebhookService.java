package com.travelci.webhook.services.webhook;


import com.travelci.webhook.entities.PayLoad;
import com.travelci.webhook.services.json.extractor.AbstractExtractor;

public interface WebhookService {

    void sendPayloadToProjectsService(String jsonPayLoad);

    AbstractExtractor findExtractor(String jsonPayLoad);

    PayLoad convertInPayLoad(AbstractExtractor extractor, String jsonPayLoad);
}
