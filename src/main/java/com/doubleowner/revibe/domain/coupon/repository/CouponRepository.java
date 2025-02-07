package com.doubleowner.revibe.domain.coupon.repository;

import com.doubleowner.revibe.domain.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
