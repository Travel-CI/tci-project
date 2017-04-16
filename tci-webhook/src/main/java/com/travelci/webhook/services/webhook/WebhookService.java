package com.travelci.webhook.services.webhook;


import com.travelci.webhook.entities.PayLoad;
import com.travelci.webhook.services.json.extractor.Extractor;

public interface WebhookService {

    void sendPayloadToProjectsService(String jsonPayLoad);

    Extractor findExtractor(String jsonPayLoad);

    PayLoad convertInPayLoad(Extractor extractor, String jsonPayLoad);
}
