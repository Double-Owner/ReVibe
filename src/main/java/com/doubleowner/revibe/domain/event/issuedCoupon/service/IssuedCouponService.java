package com.doubleowner.revibe.domain.event.issuedCoupon.service;

import com.doubleowner.revibe.domain.event.coupon.entity.Coupon;
import com.doubleowner.revibe.domain.event.coupon.repository.CouponRepository;
import com.doubleowner.revibe.domain.event.issuedCoupon.dto.IssuedCouponResponseDto;
import com.doubleowner.revibe.domain.event.issuedCoupon.entity.IssuedCoupon;
import com.doubleowner.revibe.domain.event.issuedCoupon.repository.IssuedCouponRepository;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class IssuedCouponService {

    private final IssuedCouponRepository issuedCouponRepository;
    private final CouponRepository couponRepository;
    private final UserRepository userRepository;

    public IssuedCouponResponseDto issueCoupon(Long id, User user) {

        Coupon findCoupon = couponRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("쿠폰을 찾을 수 없습니다."));

        boolean alreadyIssued = issuedCouponRepository.existsByIdAndUser(findCoupon.getId(), user);
        if(alreadyIssued){
            throw new IllegalArgumentException("이미 해당 쿠폰을 발급받았습니다.");
        }

        findCoupon.decrementCoupon();

        IssuedCoupon issuedCoupon = IssuedCoupon.builder()
                .usedAt(null)
                .createdAt(LocalDateTime.now())
                .coupon(findCoupon)
                .user(user)
                .build();

        IssuedCoupon savedIssuedCoupon = issuedCouponRepository.save(issuedCoupon);

        couponRepository.save(findCoupon);

        return toDto(savedIssuedCoupon);
    }

    private IssuedCouponResponseDto toDto(IssuedCoupon issuedCoupon) {
        return IssuedCouponResponseDto.builder()
                .userId(issuedCoupon.getUser().getId())
                .couponId(issuedCoupon.getCoupon().getId())
                .status(issuedCoupon.getStatus())
                .createdAt(issuedCoupon.getCreatedAt())
                .build();
    }

}
