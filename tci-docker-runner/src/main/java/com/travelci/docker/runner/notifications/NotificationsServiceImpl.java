package com.travelci.docker.runner.notifications;

import com.travelci.docker.runner.notifications.entities.Email;
import com.travelci.docker.runner.project.entities.ProjectDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpStatus.OK;

@Service
@Slf4j
public class NotificationsServiceImpl implements NotificationsService {

    private final RestTemplate restTemplate;
    private final String notificationsServiceUrl;

    public NotificationsServiceImpl(final RestTemplate restTemplate,
                                    @Value("${info.services.notifications}")
                                    final String notificationsServiceUrl) {
        this.restTemplate = restTemplate;
        this.notificationsServiceUrl = notificationsServiceUrl;
    }

    @Override
    public void sendSuccessNotification(final ProjectDto project) {

        if (project.getEmails() == null || project.getEmails().isEmpty())
            return;

        final String template = new StringBuilder()
            .append(project.getName() + "\n\n")
            .append("Build Successful\n")
            .append("Build Started at : " + project.getCurrentBuild().getBuildStart() + "\n")
            .append("Build ended at : " + project.getCurrentBuild().getBuildEnd() + "\n")
            .append("Commit hash : " + project.getCurrentBuild().getCommitHash() + "\n")
            .append("Commit Message : " + project.getCurrentBuild().getCommitMessage() + "\n")
            .toString();

        final Email email = Email.builder()
            .sendTo(project.getEmails())
            .subject("Build Success")
            .message(template)
            .build();

        sendNotification(email, "/success");
    }

    @Override
    public void sendErrorNotification(final ProjectDto project) {

        if (project.getEmails() == null || project.getEmails().isEmpty())
            return;

        final String template = new StringBuilder()
                .append(project.getName() + "\n\n")
                .append("Build Error\n")
                .append("Build Started at : " + project.getCurrentBuild().getBuildStart() + "\n")
                .append("Build ended at : " + project.getCurrentBuild().getBuildEnd() + "\n")
                .append("Commit hash : " + project.getCurrentBuild().getCommitHash() + "\n")
                .append("Commit Message : " + project.getCurrentBuild().getCommitMessage() + "\n")
                .toString();

        final Email email = Email.builder()
                .sendTo(project.getEmails())
                .subject("Build Error")
                .message(template)
                .build();

        sendNotification(email, "/error");
    }

    @Override
    public void sendNotification(final Email email, final String url) {
        try {
            final ResponseEntity<Void> response = restTemplate.postForEntity(
                    notificationsServiceUrl + "/email" + url, email, Void.class);

            if (!OK.equals(response.getStatusCode()))
                throw new RestClientException(
                    "Response Status Code is wrong. Expected : OK, Given : " + response.getStatusCode());
        } catch (final RestClientException e) {
            log.error(e.getMessage(), e.getCause());
        }
    }
}
