package com.travelci.logger.step.entities;

import com.travelci.logger.build.entities.Build;
import com.travelci.logger.build.entities.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "step")
public class Step {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private String command;

    @Column
    private String commandResult;

    @Column
    @Enumerated(STRING)
    private Status status;

    @Column
    private Timestamp stepStart;

    @Column
    private Timestamp stepEnd;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(nullable = false)
    private Build buildRoot;
}
