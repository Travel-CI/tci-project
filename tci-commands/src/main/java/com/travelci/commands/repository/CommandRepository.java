package com.travelci.commands.repository;

import com.travelci.commands.entities.CommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandRepository extends JpaRepository<CommandEntity, Long> {

    List<CommandEntity> findCommandEntitiesByProjectId(Integer projectId);
}
