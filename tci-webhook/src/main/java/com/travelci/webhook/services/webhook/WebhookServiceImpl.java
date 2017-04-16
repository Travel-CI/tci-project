package com.travelci.webhook.services.webhook;

import com.travelci.webhook.entities.PayLoad;
import com.travelci.webhook.exceptions.ExtractorNotFoundException;
import com.travelci.webhook.services.json.extractor.Extractor;
import org.springframework.stereotype.Service;

@Service
public class WebhookServiceImpl implements WebhookService {

    private final Extractor bitbucketExtractor;
    private final Extractor githubExtractor;

    public WebhookServiceImpl(final Extractor bitbucketExtractor,
                              final Extractor githubExtractor) {
        this.bitbucketExtractor = bitbucketExtractor;
        this.githubExtractor = githubExtractor;
    }

    @Override
    public void sendPayloadToProjectsService(final String jsonPayLoad) {

        final Extractor extractor = findExtractor(jsonPayLoad);
        final PayLoad payLoad = convertInPayLoad(extractor, jsonPayLoad);

        System.out.println(payLoad);
    }

    @Override
    public Extractor findExtractor(final String jsonPayLoad) {

        if (bitbucketExtractor.jsonHasGoodFormat(jsonPayLoad))
            return bitbucketExtractor;
        else if (githubExtractor.jsonHasGoodFormat(jsonPayLoad))
            return githubExtractor;

        throw new ExtractorNotFoundException();
    }

    @Override
    public PayLoad convertInPayLoad(final Extractor extractor, final String jsonPayLoad) {
        return extractor.transformJsonToPayload(jsonPayLoad);
    }
}
