package com.travelci.projects.services;

import com.travelci.projects.entities.PayLoad;
import com.travelci.projects.entities.ProjectDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

@Service
@RefreshScope
class GitServiceImpl implements GitService {

    @Value("{info.repositories.location}")
    private String rootRepositoriesLocation;

    public void pullProjectRepository(final ProjectDto projectDto,
                                      final PayLoad webHookPayLoad) {
        /*
        http://www.codeaffine.com/2015/05/06/jgit-initialize-repository/
        http://www.codeaffine.com/2015/11/30/jgit-clone-repository/
        http://www.codeaffine.com/2014/12/09/jgit-authentication/
        Ssh -> user + password
        Https -> OAuth token
         */
    }
}
