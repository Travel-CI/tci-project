package com.travelci.commands.entities;

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
public class ProjectDto {

    @NotNull
    private Long id;

    @NotNull @NotEmpty
    private String name;

    private String dockerfileLocation;
}
