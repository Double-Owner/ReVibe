package com.doubleowner.revibe.domain.coupon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class CouponResponseDto {

    private final Long couponId;
    private final String name;
    private final int price;
    private final int totalQuantity;
    private LocalDateTime issuedStart;
    private LocalDateTime issuedEnd;

}
