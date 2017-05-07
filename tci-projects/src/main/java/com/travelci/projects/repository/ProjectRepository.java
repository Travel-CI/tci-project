package com.travelci.projects.repository;

import com.travelci.projects.entities.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    @Query("select p from ProjectEntity p where p.enable = 1 and p.repositoryUrl = :repositoryUrl and p.branches like concat('%',:branch,'%')")
    Optional<ProjectEntity> findFromPayLoad(@Param("repositoryUrl") String repositoryUrl, @Param("branch") String branch);
}
