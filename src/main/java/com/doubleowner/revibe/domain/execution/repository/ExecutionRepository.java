package com.doubleowner.revibe.domain.execution.repository;

import com.doubleowner.revibe.domain.execution.entity.Execution;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExecutionRepository extends JpaRepository<Execution, Long> {

    @EntityGraph(attributePaths = {"sell", "payment"})
    @Query("SELECT p FROM Execution p WHERE p.id = :id")
    Optional<Execution> findExecutionById(@Param("id") Long id);
}
