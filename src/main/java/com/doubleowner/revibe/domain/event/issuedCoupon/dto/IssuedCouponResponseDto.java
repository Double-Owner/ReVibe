package com.doubleowner.revibe.domain.event.issuedCoupon.dto;

import com.doubleowner.revibe.domain.event.coupon.entity.CouponStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class IssuedCouponResponseDto {

    private final Long userId;
    private final Long couponId;
    private final CouponStatus status;
    private final LocalDateTime createdAt;

}
