package com.travelci.webhook.entities;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class PayLoad {

    private String repositoryUrl;

    private String branchName;

    private String commitAuthor;

    private String commitHash;

    private String commitMessage;

    private Date commitDate;
}
