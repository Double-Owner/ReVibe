package com.doubleowner.revibe.domain.event.issuedCoupon.repository;

import com.doubleowner.revibe.domain.event.issuedCoupon.entity.IssuedCoupon;
import com.doubleowner.revibe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssuedCouponRepository extends JpaRepository<IssuedCoupon, Long> {

    boolean existsByIdAndUser(Long id, User user);

}
