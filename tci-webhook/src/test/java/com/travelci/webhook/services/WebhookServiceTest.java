package com.travelci.webhook.services;

import com.travelci.webhook.services.json.extractor.AbstractExtractor;
import com.travelci.webhook.services.json.extractor.BitbucketExtractorImpl;
import com.travelci.webhook.services.json.extractor.GithubExtractorImpl;
import com.travelci.webhook.services.webhook.WebhookServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WebhookServiceTest {

    @InjectMocks
    private WebhookServiceImpl webhookService;

    @Mock
    private BitbucketExtractorImpl bitbucketExtractor;

    @Mock
    private GithubExtractorImpl githubExtractor;

    @Before
    public void setUp() {
        when(bitbucketExtractor.jsonHasGoodFormat("bitbucket"))
            .thenReturn(true);
        when(githubExtractor.jsonHasGoodFormat("github"))
            .thenReturn(true);
    }

    @Test
    public void shouldReturnBitbucketExtractorWhenFindExtractorWithBitbucketFormat() {

        AbstractExtractor resultExtractor = webhookService.findExtractor("bitbucket");

        assertThat(resultExtractor).isNotNull();
        assertThat(resultExtractor).isInstanceOf(BitbucketExtractorImpl.class);
    }

    @Test
    public void shouldReturnGithubExtractorWhenFindExtractorWithGithubFormat() {

        AbstractExtractor resultExtractor = webhookService.findExtractor("github");

        assertThat(resultExtractor).isNotNull();
        assertThat(resultExtractor).isInstanceOf(GithubExtractorImpl.class);
    }
}
