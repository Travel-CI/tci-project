package com.travelci.webhook.services.json.extractor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelci.webhook.entities.PayLoad;
import com.travelci.webhook.exceptions.ExtractorWrongFormatException;
import com.travelci.webhook.exceptions.ValidatorException;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public abstract class AbstractExtractor {

    private final List<String> identifiers;
    private final String repositoryUrlJsonTree;
    private final String branchNameJsonTree;
    private final String commitAuthorJsonTree;
    private final String commitHashJsonTree;
    private final String commitMessageJsonTree;
    private final String commitDateJsonTree;

    private final Validator validator;

    public AbstractExtractor(final List<String> identifiers,
                             final String repositoryUrlJsonTree,
                             final String branchNameJsonTree,
                             final String commitAuthorJsonTree,
                             final String commitHashJsonTree,
                             final String commitMessageJsonTree,
                             final String commitDateJsonTree,
                             final Validator validator) {

        this.identifiers = identifiers;
        this.repositoryUrlJsonTree = repositoryUrlJsonTree;
        this.branchNameJsonTree = branchNameJsonTree;
        this.commitAuthorJsonTree = commitAuthorJsonTree;
        this.commitHashJsonTree = commitHashJsonTree;
        this.commitMessageJsonTree = commitMessageJsonTree;
        this.commitDateJsonTree = commitDateJsonTree;
        this.validator = validator;
    }

    public boolean jsonHasGoodFormat(final String jsonPayLoad) {
        return identifiers.stream().allMatch(jsonPayLoad::contains);
    }

    public PayLoad transformJsonToPayload(final String jsonPayLoad)
        throws ExtractorWrongFormatException {

        try {
            final ObjectMapper mapper = new ObjectMapper();
            final JsonNode reader = mapper.readTree(jsonPayLoad);

            PayLoad payLoad =  PayLoad.builder()
                    .repositoryUrl(reader.at(repositoryUrlJsonTree).asText())
                    .branchName(reader.at(branchNameJsonTree).asText())
                    .commitAuthor(reader.at(commitAuthorJsonTree).asText())
                    .commitHash(reader.at(commitHashJsonTree).asText())
                    .commitMessage(reader.at(commitMessageJsonTree).asText())
                    .commitDate(null /* reader.at(commitDateJsonTree) */) // TODO Add date
                    .build();

            final Set<ConstraintViolation<PayLoad>> constraintViolations = validator.validate(payLoad);
            if (!constraintViolations.isEmpty())
                throw new ValidatorException(constraintViolations);

            return payLoad;
        } catch (IOException | ValidatorException e) {
            throw new ExtractorWrongFormatException(e.getMessage());
        }
    }
}
