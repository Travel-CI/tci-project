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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WebhookServiceTest {

    @Mock private BitbucketExtractorImpl bitbucketExtractor;
    @Mock private GithubExtractorImpl githubExtractor;
    @Mock private RestTemplate restTemplate;

    private WebhookServiceImpl webhookService;

    @BeforeEach
    void setUp() {
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
