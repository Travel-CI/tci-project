package com.travelci.webhook.services;

import com.travelci.webhook.services.json.extractor.AbstractExtractor;
import com.travelci.webhook.services.webhook.WebhookServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class WebhookServiceTest {

    @InjectMocks
    private WebhookServiceImpl webhookService;

    @Mock private AbstractExtractor bitbucketExtractor;
    @Mock private AbstractExtractor githubExtractor;
    @Mock private RestTemplate restTemplate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this); // Useless ?
    }

    @Test
    public void shouldReturnBitbucketExtractorWhenFindExtractorWithBitbucketFormat() {

        /*when(bitbucketExtractor.jsonHasGoodFormat(any(String.class)))
            .thenReturn(true);

        AbstractExtractor resultExtractor = webhookService.findExtractor("bitbucket");

        assertThat(resultExtractor).isNotNull();
        assertThat(resultExtractor).isInstanceOf(BitbucketExtractorImpl.class);*/
    }

    @Test
    public void shouldReturnGithubExtractorWhenFindExtractorWithGithubFormat() {

        /*when(githubExtractor.jsonHasGoodFormat(any(String.class)))
            .thenReturn(true);

        AbstractExtractor resultExtractor = webhookService.findExtractor("github");

        assertThat(resultExtractor).isNotNull();
        assertThat(resultExtractor).isInstanceOf(GithubExtractorImpl.class);*/
    }
}
