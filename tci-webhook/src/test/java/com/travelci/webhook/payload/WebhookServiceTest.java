package com.travelci.webhook.payload;

import com.travelci.webhook.extractor.AbstractExtractor;
import com.travelci.webhook.extractor.BitbucketExtractorImpl;
import com.travelci.webhook.extractor.GithubExtractorImpl;
import com.travelci.webhook.extractor.exceptions.ExtractorNotFoundException;
import com.travelci.webhook.extractor.exceptions.ExtractorWrongFormatException;
import com.travelci.webhook.payload.entities.PayLoad;
import com.travelci.webhook.payload.exceptions.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WebhookServiceTest {

    private BitbucketExtractorImpl bitbucketExtractor;
    private GithubExtractorImpl githubExtractor;
    private RestTemplate restTemplate;

    private WebhookServiceImpl webhookService;

    @BeforeEach
    void setUp() {
        bitbucketExtractor = mock(BitbucketExtractorImpl.class);
        githubExtractor = mock(GithubExtractorImpl.class);
        restTemplate = mock(RestTemplate.class);

        webhookService = new WebhookServiceImpl(bitbucketExtractor, githubExtractor,
            restTemplate, "");
    }

    @Test
    void shouldReturnBitbucketExtractorWhenFindExtractorWithBitbucketFormat() {

        when(bitbucketExtractor.jsonHasGoodFormat(any(String.class)))
            .thenReturn(true);

        AbstractExtractor resultExtractor = webhookService.findExtractor("bitbucket");

        assertThat(resultExtractor).isNotNull();
        assertThat(resultExtractor).isInstanceOf(BitbucketExtractorImpl.class);
    }

    @Test
    void shouldReturnGithubExtractorWhenFindExtractorWithGithubFormat() {

        when(githubExtractor.jsonHasGoodFormat(any(String.class)))
            .thenReturn(true);

        AbstractExtractor resultExtractor = webhookService.findExtractor("github");

        assertThat(resultExtractor).isNotNull();
        assertThat(resultExtractor).isInstanceOf(GithubExtractorImpl.class);
    }

    @Test
    void shouldThrowExceptionWhenFindExtractorWithAnUnknownJsonFormat() {
        assertThrows(ExtractorNotFoundException.class,
            () -> webhookService.findExtractor("unknownJson")
        );
    }

    @Test
    void shouldReturnPayLoadGivenJsonGoodFormatWhenTransformJsonToPayLoad()
        throws ExtractorWrongFormatException {

        when(bitbucketExtractor.transformJsonToPayload(any(String.class)))
            .thenReturn(PayLoad.builder().commitMessage("test").build());

        PayLoad payLoad = webhookService.convertInPayLoad(bitbucketExtractor, "bitbucket");

        assertThat(payLoad).isNotNull();
        assertThat(payLoad.getCommitMessage()).isEqualTo("test");
    }

    @Test
    void shouldThrowExceptionWhenTransformJsonToPayloadFailed()
        throws ExtractorWrongFormatException {

        when(bitbucketExtractor.transformJsonToPayload(any(String.class)))
            .thenThrow(ExtractorWrongFormatException.class);

        assertThrows(BadRequestException.class,
            () -> webhookService.convertInPayLoad(bitbucketExtractor, "bitbucket")
        );
    }
}
