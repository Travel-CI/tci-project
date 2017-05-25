package com.travelci.webhook.services.webhook;

import com.travelci.webhook.entities.PayLoad;
import com.travelci.webhook.exceptions.BadRequestException;
import com.travelci.webhook.exceptions.ExtractorNotFoundException;
import com.travelci.webhook.exceptions.ExtractorWrongFormatException;
import com.travelci.webhook.services.json.extractor.AbstractExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpStatus.ACCEPTED;

@Service
@RefreshScope
public class WebhookServiceImpl implements WebhookService {

    private final AbstractExtractor bitbucketExtractor;
    private final AbstractExtractor githubExtractor;
    private final RestTemplate restTemplate;

    private final String projectsServiceUrl;
    private final String loggerServiceUrl;

    public WebhookServiceImpl(final AbstractExtractor bitbucketExtractor,
                              final AbstractExtractor githubExtractor,
                              final RestTemplate restTemplate,
                              @Value("${info.services.projects}") final String projectsServiceUrl,
                              @Value("${info.services.logger") final String loggerServiceUrl) {
        this.bitbucketExtractor = bitbucketExtractor;
        this.githubExtractor = githubExtractor;
        this.restTemplate = restTemplate;
        this.projectsServiceUrl = projectsServiceUrl;
        this.loggerServiceUrl = loggerServiceUrl;
    }

    @Override
    public void sendPayloadToProjectsService(final String jsonPayLoad) {

        final AbstractExtractor extractor = findExtractor(jsonPayLoad);
        final PayLoad payLoad = convertInPayLoad(extractor, jsonPayLoad);

        final ResponseEntity<Void> response = restTemplate.postForEntity(
            projectsServiceUrl + "/projects/webhook",
            payLoad,
            Void.class
        );

        if (!ACCEPTED.equals(response.getStatusCode())) {
            // TODO Call logger service
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

            System.out.println(e.getMessage());
            // TODO Call Logger Service to Log this error
            //restTemplate.postForObject(loggerServiceUrl, payLoad, null);

            throw new BadRequestException();
        }
    }
}
