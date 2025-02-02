package com.doubleowner.revibe.domain.coupon.repository;

import com.doubleowner.revibe.domain.coupon.entity.IssuedCoupon;
import com.doubleowner.revibe.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IssuedCouponRepository extends JpaRepository<IssuedCoupon, Long> {

    boolean existsByIdAndUser(Long id, User user);

    List<IssuedCoupon> findByUser(User user, Pageable pageable);

    Optional<IssuedCoupon> findByIdAndUser(Long id, User user);
}
