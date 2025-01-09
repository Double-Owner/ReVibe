package com.doubleowner.revibe.domain.payment.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PayStatus {
    PAY_SUCCESS("pay_success"),
    PAY_PENDING("pay_pending"),
    PAY_CANCELED("pay_canceled");

    private final String value;
}
