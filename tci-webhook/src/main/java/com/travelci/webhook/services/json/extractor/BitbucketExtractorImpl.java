package com.travelci.webhook.services.json.extractor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelci.webhook.entities.PayLoad;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service("bitbucketExtractor")
@RefreshScope
public class BitbucketExtractorImpl implements Extractor {

    @Value("#{'${bitbucket.identifiers}'.split(',')}")
    private List<String> identifiers;
    @Value("${bitbucket.repository.url}")
    private String repositoryUrlJsonTree;
    @Value("${bitbucket.branch.name}")
    private String branchNameJsonTree;
    @Value("${bitbucket.commit.author}")
    private String commitAuthorJsonTree;
    @Value("${bitbucket.commit.hash}")
    private String commitHashJsonTree;
    @Value("${bitbucket.commit.message}")
    private String commitMessageJsonTree;
    @Value("${bitbucket.commit.date}")
    private String commitDateJsonTree;

    @Override
    public boolean jsonHasGoodFormat(String jsonPayLoad) {
        return identifiers.stream().anyMatch(jsonPayLoad::contains);
    }

    @Override
    public PayLoad transformJsonToPayload(final String jsonPayLoad) {

        final ObjectMapper mapper = new ObjectMapper();

        try {
            final JsonNode reader = mapper.readTree(jsonPayLoad);

            return PayLoad.builder()
                    .repositoryUrl(reader.at(repositoryUrlJsonTree).asText())
                    .branchName(reader.at(branchNameJsonTree).asText())
                    .commitAuthor(reader.at(commitAuthorJsonTree).asText())
                    .commitHash(reader.at(commitHashJsonTree).asText())
                    .commitMessage(reader.at(commitMessageJsonTree).asText())
                    .commitDate(null /* reader.at(commitDateJsonTree) */)
                .build();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
