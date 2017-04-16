package com.travelci.webhook.services.json.extractor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelci.webhook.entities.PayLoad;
import com.travelci.webhook.exceptions.ExtractorWrongFormatException;
import com.travelci.webhook.exceptions.ValidatorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.util.List;
import java.util.Set;

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

    private Validator validator;

    public BitbucketExtractorImpl(final Validator validator) {
        this.validator = validator;
    }

    @Override
    public boolean jsonHasGoodFormat(final String jsonPayLoad) {
        return identifiers.stream().anyMatch(jsonPayLoad::contains);
    }

    @Override
    public PayLoad transformJsonToPayload(final String jsonPayLoad) throws ExtractorWrongFormatException {

        try {
            final ObjectMapper mapper = new ObjectMapper();
            final JsonNode reader = mapper.readTree(jsonPayLoad);

            PayLoad payLoad =  PayLoad.builder()
                .repositoryUrl(reader.at(repositoryUrlJsonTree).asText())
                .branchName(reader.at(branchNameJsonTree).asText())
                .commitAuthor(reader.at(commitAuthorJsonTree).asText())
                .commitHash(reader.at(commitHashJsonTree).asText())
                .commitMessage(reader.at(commitMessageJsonTree).asText())
                .commitDate(null /* reader.at(commitDateJsonTree) */)
            .build();

            Set<ConstraintViolation<PayLoad>> constraintViolations = validator.validate(payLoad);
            if (!constraintViolations.isEmpty())
                throw new ValidatorException(constraintViolations);

            return payLoad;

        } catch (IOException | ValidatorException e) {
            throw new ExtractorWrongFormatException(e.getMessage());
        }
    }
}
