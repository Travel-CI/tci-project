package com.travelci.commands.command.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "command")
public class Command {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private String command;

    @Column
    private Long projectId;

    @Column
    private Integer commandOrder;

    @Column
    private Boolean enabled;

    @Column
    private Boolean enableLogs;
}
