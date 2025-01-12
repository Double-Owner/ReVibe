package com.doubleowner.revibe.domain.payment.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PayMethod {
    CASH("PAYMENT_CASH"),
    CREDIT_CARD("PAYMENT_CREDIT_CARD"),
    TOSS_PAY("PAYMENT_TOSS_PAY");

    private final String value;
}
