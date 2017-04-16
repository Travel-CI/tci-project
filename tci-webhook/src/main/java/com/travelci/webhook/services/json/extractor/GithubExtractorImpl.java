package com.travelci.webhook.services.json.extractor;

import com.travelci.webhook.entities.PayLoad;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

@Service("githubExtractor")
@RefreshScope
public class GithubExtractorImpl implements Extractor {

    @Override
    public boolean jsonHasGoodFormat(String jsonPayLoad) {
        return false;
    }

    @Override
    public PayLoad transformJsonToPayload(String jsonPayLoad) {
        return null;
    }
}