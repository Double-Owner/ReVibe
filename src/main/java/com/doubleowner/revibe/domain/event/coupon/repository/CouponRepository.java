package com.doubleowner.revibe.domain.event.coupon.repository;

import com.doubleowner.revibe.domain.event.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
