package com.travelci.logger.build;

import com.travelci.logger.build.entities.Build;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface BuildRepository extends JpaRepository<Build, Long> {

    Optional<Build> findById(Long id);

    List<Build> findByProjectId(Long projectId);

    Long deleteByProjectId(Long projectId);
}
