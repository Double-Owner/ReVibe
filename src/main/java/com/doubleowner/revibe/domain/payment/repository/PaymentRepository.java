package com.doubleowner.revibe.domain.payment.repository;

import com.doubleowner.revibe.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {


    @EntityGraph(attributePaths = {"buy"})
    @Query("SELECT p FROM Payment p WHERE p.id = :id")
    Optional<Payment> findByPaymentId(@Param("id") Long id);

}
