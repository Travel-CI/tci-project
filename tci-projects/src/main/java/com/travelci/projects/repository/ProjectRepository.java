package com.travelci.projects.repository;

import com.travelci.projects.entities.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    Optional<ProjectEntity> findById(Long id);

    @Query("select p from ProjectEntity p where p.enable = 1 and p.repositoryUrl = :repositoryUrl and p.branches like concat('%',:branch,'%')")
    Optional<ProjectEntity> findFromPayLoad(@Param("repositoryUrl") String repositoryUrl, @Param("branch") String branch);
}

// TODO Change bdd
// https://spring.io/guides/gs/accessing-data-mongodb/
// https://github.com/fakemongo/fongo
// https://logging.apache.org/log4j/2.x/manual/appenders.html#NoSQLAppender