package com.doubleowner.revibe.domain.execution.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExecutionStatus {
    WAITING_FOR_PAYMENT("WAIT"),
    PAYMENT_SUCCESS("SUCCESS");

    private final String value;
}
