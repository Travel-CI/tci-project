package com.travelci.projects.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "project")
public class Project {

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

    @Column
    @Convert(converter = BranchListConverter.class)
    private List<String> branches;

    @Column
    private String userName;

    @Column
    private String userPassword;

    @Column
    private String repositoryToken;

    @Column
    private String dockerfileLocation;

    @Column
    private Date lastStart;

    @Column
    private Timestamp created;

    @Column
    private Timestamp updated;
}
