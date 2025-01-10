package com.doubleowner.revibe.global.common.enumType;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BidStatus {

    ONPROGRESS("Bid_onprogress"),
    END("Bid_end");

    private final String value;
}