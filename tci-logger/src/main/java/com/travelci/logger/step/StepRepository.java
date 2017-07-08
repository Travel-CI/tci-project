package com.travelci.logger.step;

import com.travelci.logger.step.entities.Step;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface StepRepository extends JpaRepository<Step, Long> {

    Optional<Step> findById(Long id);

    List<Step> findByBuildRoot(Long buildId);
}
