package com.travelci.projects.entities;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Builder
@Data
@Table(name = "project")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Boolean enable;

    @Column
    private String repositoryUrl;

    @Transient
    private List<String> branches;

    @Column
    private String dockerFileLocation;

    @Column
    private Date lastStart;

    @Column
    private Date created;

    @Column
    private Date updated;
}
