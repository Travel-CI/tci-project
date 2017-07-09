package com.travelci.webhook.payload;

import com.travelci.webhook.payload.exceptions.BadRequestException;
import com.travelci.webhook.extractor.exceptions.ExtractorNotFoundException;
import com.travelci.webhook.extractor.exceptions.ExtractorWrongFormatException;
import com.travelci.webhook.payload.entities.PayLoad;
import com.travelci.webhook.extractor.AbstractExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpStatus.ACCEPTED;

@Service
@RefreshScope
class WebhookServiceImpl implements WebhookService {

    private final AbstractExtractor bitbucketExtractor;
    private final AbstractExtractor githubExtractor;
    private final RestTemplate restTemplate;

    private final String projectsServiceUrl;

    WebhookServiceImpl(final AbstractExtractor bitbucketExtractor,
                       final AbstractExtractor githubExtractor,
                       final RestTemplate restTemplate,
                       @Value("${info.services.projects}") final String projectsServiceUrl) {
        this.bitbucketExtractor = bitbucketExtractor;
        this.githubExtractor = githubExtractor;
        this.restTemplate = restTemplate;
        this.projectsServiceUrl = projectsServiceUrl;
    }

    @Override
    public void sendPayloadToProjectsService(final String jsonPayLoad) {

        final AbstractExtractor extractor = findExtractor(jsonPayLoad);
        final PayLoad payLoad = convertInPayLoad(extractor, jsonPayLoad);

        try {
            final ResponseEntity<Void> response = restTemplate.postForEntity(
                projectsServiceUrl + "/projects/webhook",
                payLoad,
                Void.class
            );

            if (!ACCEPTED.equals(response.getStatusCode()))
                throw new RestClientException(
                    "Response Status Code is wrong. Expected : ACCEPTED, Given : " + response.getStatusCode()
                );
        } catch (RestClientException e) {
            throw new BadRequestException();
        }
    }

    @Override
    public AbstractExtractor findExtractor(final String jsonPayLoad) {

        if (bitbucketExtractor.jsonHasGoodFormat(jsonPayLoad))
            return bitbucketExtractor;
        else if (githubExtractor.jsonHasGoodFormat(jsonPayLoad))
            return githubExtractor;

        throw new ExtractorNotFoundException();
    }

    @Override
    public PayLoad convertInPayLoad(final AbstractExtractor extractor,
                                    final String jsonPayLoad) {

        try {
            return extractor.transformJsonToPayload(jsonPayLoad);
        } catch (ExtractorWrongFormatException e) {
            throw new BadRequestException();
        }
    }
}
