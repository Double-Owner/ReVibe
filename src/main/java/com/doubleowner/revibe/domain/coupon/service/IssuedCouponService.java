package com.doubleowner.revibe.domain.coupon.service;

import com.doubleowner.revibe.domain.coupon.entity.Coupon;
import com.doubleowner.revibe.domain.coupon.entity.IssuedCoupon;
import com.doubleowner.revibe.domain.coupon.repository.CouponRepository;
import com.doubleowner.revibe.domain.coupon.repository.IssuedCouponRepository;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.global.aop.DistributedLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IssuedCouponService {

    private final CouponRepository couponRepository;
    private final IssuedCouponRepository  issuedCouponRepository;

    @DistributedLock(key = "#couponId")
    public void issuedCoupon(Long id, User user) {
        Coupon findCoupon = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("쿠폰이 존재하지 않습니다."));

        IssuedCoupon issuedCoupon = IssuedCoupon.builder()
                .coupon(findCoupon)
                .user(user)
                .build();

        extracted(findCoupon, issuedCoupon);
}

    @Transactional
    public void extracted(Coupon findCoupon, IssuedCoupon issuedCoupon) {
        findCoupon.decrementCoupon();
        System.out.println("현재 쿠폰 개수 : "+ findCoupon.getTotalQuantity());
        couponRepository.save(findCoupon);
    }
}