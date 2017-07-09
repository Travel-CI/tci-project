package com.travelci.logger.build.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "build")
public class Build {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long projectId;

    @Column
    private Timestamp buildStart;

    @Column
    private Timestamp buildEnd;

    @Column
    private String startBy;

    @Column
    private String commitHash;

    @Column
    private String commitMessage;

    @Column
    private String branch;

    @Column
    private String error;

    @Column
    @Enumerated(STRING)
    private Status status;
}
