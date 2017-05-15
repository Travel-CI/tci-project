package com.travelci.webhook.services.webhook;

import com.travelci.webhook.exceptions.ExtractorNotFoundException;
import com.travelci.webhook.services.json.extractor.AbstractExtractor;
import com.travelci.webhook.services.json.extractor.BitbucketExtractorImpl;
import com.travelci.webhook.services.json.extractor.GithubExtractorImpl;
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
            restTemplate, "", "");
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
}
