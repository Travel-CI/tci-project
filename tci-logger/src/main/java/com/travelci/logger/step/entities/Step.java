package com.travelci.logger.step.entities;

import com.travelci.logger.build.entities.Build;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "step")
public class Step {

    @Id
    private Long id;

    @Column
    private String command;

    @Column
    private String commandResult;

    @Column
    private StepStatus stepStatus;

    @Column
    private Timestamp stepStart;

    @Column
    private Timestamp stepEnd;

    @OneToOne
    private Build buildRoot;
}
