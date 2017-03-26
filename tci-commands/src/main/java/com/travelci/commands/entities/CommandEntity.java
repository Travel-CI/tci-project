package com.travelci.commands.entities;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Builder
@Data
@Table(name = "command")
public class CommandEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String command;

    @Column
    private Integer projectId;

    @Column
    private Integer order;

    @Column
    private Boolean enabled;

    @Column
    private Boolean enableLogs;
}
