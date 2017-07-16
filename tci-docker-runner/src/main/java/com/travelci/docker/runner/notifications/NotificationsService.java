package com.travelci.docker.runner.notifications;

import com.travelci.docker.runner.notifications.entities.Email;
import com.travelci.docker.runner.project.entities.ProjectDto;

public interface NotificationsService {
    void sendSuccessNotification(ProjectDto project);
    void sendErrorNotification(ProjectDto project);
    void sendNotification(final Email email, final String url);
}
