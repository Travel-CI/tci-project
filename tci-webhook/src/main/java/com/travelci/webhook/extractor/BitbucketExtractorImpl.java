package com.travelci.webhook.extractor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.List;

@Service("bitbucketExtractor")
@RefreshScope
public class BitbucketExtractorImpl extends AbstractExtractor {

    public BitbucketExtractorImpl(@Value("#{'${bitbucket.identifiers}'.split(',')}") final List<String> identifiers,
                                  @Value("${bitbucket.repository.url}") final String repositoryUrlJsonTree,
                                  @Value("${bitbucket.branch.name}") final String branchNameJsonTree,
                                  @Value("${bitbucket.commit.author}") final String commitAuthorJsonTree,
                                  @Value("${bitbucket.commit.hash}") final String commitHashJsonTree,
                                  @Value("${bitbucket.commit.message}") final String commitMessageJsonTree,
                                  final Validator validator) {

        super(identifiers, repositoryUrlJsonTree,
            branchNameJsonTree, commitAuthorJsonTree,
            commitHashJsonTree, commitMessageJsonTree,
            validator);
    }
}