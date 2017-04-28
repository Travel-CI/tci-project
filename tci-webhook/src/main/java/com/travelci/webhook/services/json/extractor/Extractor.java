package com.travelci.webhook.services.json.extractor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelci.webhook.entities.PayLoad;
import com.travelci.webhook.exceptions.ExtractorWrongFormatException;
import com.travelci.webhook.exceptions.ValidatorException;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public abstract class Extractor {


    private List<String> identifiers;
    private String repositoryUrlJsonTree;
    private String branchNameJsonTree;
    private String commitAuthorJsonTree;
    private String commitHashJsonTree;
    private String commitMessageJsonTree;
    private String commitDateJsonTree;

    private Validator validator;

    public Extractor(List<String> identifiers,
                     final String repositoryUrlJsonTree,
                     final String branchNameJsonTree,
                     final String commitAuthorJsonTree,
                     final String commitHashJsonTree,
                     final String commitMessageJsonTree,
                     final String commitDateJsonTree,
                     final Validator validator){

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
        return identifiers.stream().anyMatch(jsonPayLoad::contains);
    }


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
