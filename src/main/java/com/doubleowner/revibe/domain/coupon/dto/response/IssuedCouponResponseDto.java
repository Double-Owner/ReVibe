package com.doubleowner.revibe.domain.coupon.dto.response;

import com.doubleowner.revibe.domain.coupon.entity.CouponStatus;
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
