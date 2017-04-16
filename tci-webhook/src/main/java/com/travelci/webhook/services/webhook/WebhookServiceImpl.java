package com.travelci.webhook.services.webhook;

import com.travelci.webhook.entities.PayLoad;
import com.travelci.webhook.exceptions.BadRequestException;
import com.travelci.webhook.exceptions.ExtractorNotFoundException;
import com.travelci.webhook.exceptions.ExtractorWrongFormatException;
import com.travelci.webhook.services.json.extractor.Extractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RefreshScope
public class WebhookServiceImpl implements WebhookService {

    private final Extractor bitbucketExtractor;
    private final Extractor githubExtractor;
    private final RestTemplate restTemplate;

    @Value("${info.services.projects}")
    private String projectsServiceUrl;
    @Value("${info.services.logger")
    private String loggerServiceUrl;

    public WebhookServiceImpl(final Extractor bitbucketExtractor,
                              final Extractor githubExtractor,
                              final RestTemplate restTemplate) {
        this.bitbucketExtractor = bitbucketExtractor;
        this.githubExtractor = githubExtractor;
        this.restTemplate = restTemplate;
    }

    @Override
    public void sendPayloadToProjectsService(final String jsonPayLoad) {

        final Extractor extractor = findExtractor(jsonPayLoad);
        final PayLoad payLoad = convertInPayLoad(extractor, jsonPayLoad);

        System.out.println(payLoad);
        // Call Projects Service to send the payload
        //restTemplate.postForObject(projectsServiceUrl, payLoad, null);
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
    public PayLoad convertInPayLoad(final Extractor extractor,
                                    final String jsonPayLoad) {

        try {
            return extractor.transformJsonToPayload(jsonPayLoad);
        } catch (ExtractorWrongFormatException e) {

            System.out.println(e.getMessage());
            // Call Logger Service to Log this error
            //restTemplate.postForObject(loggerServiceUrl, payLoad, null);

            throw new BadRequestException();
        }
    }
}
