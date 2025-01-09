package com.doubleowner.revibe.domain.payment.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PayMethod {
    CASH("payment_cash"),
    CREDIT_CARD("payment_creditCard"),
    TOSS_PAY("payment_tossPay");

    private final String value;
}
