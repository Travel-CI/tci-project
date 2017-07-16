package com.travelci.docker.runner.notifications.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Email {

    @NotNull @NotEmpty
    private String sendTo;

    @NotNull @NotEmpty
    private String subject;

    @NotNull @NotEmpty
    private String message;
}
