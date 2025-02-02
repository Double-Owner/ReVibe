package com.doubleowner.revibe.domain.coupon.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponStatus {

    ACTIVE("ACTIVE"), // 유효기간 중
    USED("USED"), // 사용 완료
    EXPIRED("EXPIRED"); // 기간 만료

    private final String value;
}
