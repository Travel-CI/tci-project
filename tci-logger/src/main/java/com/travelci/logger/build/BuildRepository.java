package com.travelci.logger.build;

import com.travelci.logger.build.entities.Build;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface BuildRepository extends JpaRepository<Build, Long> {

    Optional<Build> findById(Long id);

    List<Build> findByProjectId(Long projectId);

    @Query(value = "select * from build where project_id = :projectId order by id desc limit 1", nativeQuery = true)
    Optional<Build> findLastBuild(@Param("projectId") Long projectId);

    Long deleteByProjectId(Long projectId);
}
