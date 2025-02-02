package com.doubleowner.revibe.domain.coupon.dto.response;

import com.doubleowner.revibe.domain.coupon.entity.CouponStatus;
import com.doubleowner.revibe.domain.coupon.entity.IssuedCoupon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class IssuedCouponResponseDto {

    private Long issuedCouponId;
    private Long userId;
    private Long couponId;
    private String couponName;
    private CouponStatus status;
    private LocalDateTime usedAt;
    private LocalDateTime createdAt;

    public IssuedCouponResponseDto(IssuedCoupon issuedCoupon) {
        this.issuedCouponId = issuedCoupon.getId();
        this.couponId = issuedCoupon.getCoupon().getId();
        this.userId = issuedCoupon.getUser().getId();
        this.couponName = issuedCoupon.getCoupon().getName();
        this.status = CouponStatus.ACTIVE;
        this.usedAt = issuedCoupon.getUsedAt();
        this.createdAt = issuedCoupon.getCreatedAt();
    }
}
