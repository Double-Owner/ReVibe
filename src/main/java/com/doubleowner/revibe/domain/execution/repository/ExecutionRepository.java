package com.doubleowner.revibe.domain.execution.repository;

import com.doubleowner.revibe.domain.execution.entity.Execution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutionRepository extends JpaRepository<Execution, Long> {
}
