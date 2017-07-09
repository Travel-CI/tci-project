package com.travelci.webhook.payload;

import com.travelci.webhook.extractor.AbstractExtractor;
import com.travelci.webhook.extractor.BitbucketExtractorImpl;
import com.travelci.webhook.extractor.GithubExtractorImpl;
import com.travelci.webhook.extractor.exceptions.ExtractorNotFoundException;
import com.travelci.webhook.extractor.exceptions.ExtractorWrongFormatException;
import com.travelci.webhook.payload.entities.PayLoad;
import com.travelci.webhook.payload.exceptions.BadRequestException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WebhookServiceTest {

    @Mock private BitbucketExtractorImpl bitbucketExtractor;
    @Mock private GithubExtractorImpl githubExtractor;
    @Mock private RestTemplate restTemplate;

    private WebhookServiceImpl webhookService;

    @Before
    public void setUp() {
        webhookService = new WebhookServiceImpl(bitbucketExtractor, githubExtractor,
            restTemplate, "");
    }

    @Test
    public void shouldReturnBitbucketExtractorWhenFindExtractorWithBitbucketFormat() {

        when(bitbucketExtractor.jsonHasGoodFormat(any(String.class)))
            .thenReturn(true);

        AbstractExtractor resultExtractor = webhookService.findExtractor("bitbucket");

        assertThat(resultExtractor).isNotNull();
        assertThat(resultExtractor).isInstanceOf(BitbucketExtractorImpl.class);
    }

    @Test
    public void shouldReturnGithubExtractorWhenFindExtractorWithGithubFormat() {

        when(githubExtractor.jsonHasGoodFormat(any(String.class)))
            .thenReturn(true);

        AbstractExtractor resultExtractor = webhookService.findExtractor("github");

        assertThat(resultExtractor).isNotNull();
        assertThat(resultExtractor).isInstanceOf(GithubExtractorImpl.class);
    }

    @Test(expected = ExtractorNotFoundException.class)
    public void shouldThrowExceptionWhenFindExtractorWithAnUnknownJsonFormat() {
        webhookService.findExtractor("unknownJson");
    }

    @Test
    public void shouldReturnPayLoadGivenJsonGoodFormatWhenTransformJsonToPayLoad()
        throws ExtractorWrongFormatException {

        when(bitbucketExtractor.transformJsonToPayload(any(String.class)))
            .thenReturn(PayLoad.builder().commitMessage("test").build());

        PayLoad payLoad = webhookService.convertInPayLoad(bitbucketExtractor, "bitbucket");

        assertThat(payLoad).isNotNull();
        assertThat(payLoad.getCommitMessage()).isEqualTo("test");
    }

    @SuppressWarnings("unchecked")
    @Test(expected = BadRequestException.class)
    public void shouldThrowExceptionWhenTransformJsonToPayloadFailed()
        throws ExtractorWrongFormatException {

        when(bitbucketExtractor.transformJsonToPayload(any(String.class)))
            .thenThrow(ExtractorWrongFormatException.class);

        webhookService.convertInPayLoad(bitbucketExtractor, "bitbucket");
    }
}
