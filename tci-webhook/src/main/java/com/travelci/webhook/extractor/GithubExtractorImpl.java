package com.travelci.webhook.extractor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.List;


@Service("githubExtractor")
@RefreshScope
public class GithubExtractorImpl extends AbstractExtractor {

    public GithubExtractorImpl(@Value("#{'${github.identifiers}'.split(',')}") final List<String> identifiers,
                               @Value("${github.repository.url}") final String repositoryUrlJsonTree,
                               @Value("${github.branch.name}") final String branchNameJsonTree,
                               @Value("${github.commit.author}") final String commitAuthorJsonTree,
                               @Value("${github.commit.hash}") final String commitHashJsonTree,
                               @Value("${github.commit.message}") final String commitMessageJsonTree,
                               final Validator validator) {

        super(identifiers, repositoryUrlJsonTree,
            branchNameJsonTree, commitAuthorJsonTree,
            commitHashJsonTree, commitMessageJsonTree,
            validator);
    }
}