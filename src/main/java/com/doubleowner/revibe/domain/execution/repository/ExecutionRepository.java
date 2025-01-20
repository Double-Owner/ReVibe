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

    @Query("SELECT e FROM Execution e JOIN FETCH e.sell s JOIN FETCH s.options o JOIN FETCH o.item where e.id=:id and e.payment.id=:paymentId and e.payment.buy.user.email=:email")
    Optional<Execution> findExecutionById(@Param("id") Long ExecutionId ,@Param("paymentId")Long paymentId,@Param("email") String email);
}
