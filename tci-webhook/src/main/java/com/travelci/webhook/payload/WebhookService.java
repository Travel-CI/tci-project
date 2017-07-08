package com.travelci.webhook.payload;


import com.travelci.webhook.payload.entities.PayLoad;
import com.travelci.webhook.extractor.AbstractExtractor;

interface WebhookService {

    void sendPayloadToProjectsService(String jsonPayLoad);

    AbstractExtractor findExtractor(String jsonPayLoad);

    PayLoad convertInPayLoad(AbstractExtractor extractor, String jsonPayLoad);
}
