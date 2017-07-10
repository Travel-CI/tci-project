package com.travelci.logger.step;

import com.travelci.logger.step.entities.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface StepRepository extends JpaRepository<Step, Long> {

    Optional<Step> findById(Long id);

    List<Step> findByBuildId(Long buildId);
}
