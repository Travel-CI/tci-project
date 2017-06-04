package com.travelci.commands.repository;

import com.travelci.commands.entities.Command;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommandRepository extends JpaRepository<Command, Long> {

    Optional<Command> findById(Long id);

    Optional<Command> findByProjectIdAndCommandOrder(Long projectId, Integer commandOrder);

    List<Command> findByProjectIdOrderByCommandOrderAsc(Long projectId);
}
