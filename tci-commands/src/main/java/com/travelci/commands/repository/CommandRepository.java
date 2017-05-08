package com.travelci.commands.repository;

import com.travelci.commands.entities.CommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommandRepository extends JpaRepository<CommandEntity, Long> {

    Optional<CommandEntity> findById(Long id);

    Optional<CommandEntity> findByProjectIdAndCommandOrder(Long projectId, Integer commandOrder);

    List<CommandEntity> findByProjectIdOrderByCommandOrderAsc(Long projectId);
}
