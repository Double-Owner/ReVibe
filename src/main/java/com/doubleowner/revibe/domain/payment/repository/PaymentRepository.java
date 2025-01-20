package com.doubleowner.revibe.domain.payment.repository;

import com.doubleowner.revibe.domain.payment.entity.Payment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p WHERE p.buy.user.email = :email AND p.id = :paymentId")
    Optional<Payment> findByPaymentId(@Param("paymentId") Long id, @Param("email") String email);


    @Query("SELECT p FROM Payment p where p.buy.user.id=:userId")
    List<Payment> findPaymentByUserId(@Param("userId") Long userId, Pageable pageable);

}
