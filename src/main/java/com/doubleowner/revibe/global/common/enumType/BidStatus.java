package com.doubleowner.revibe.global.common.enumType;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor

public enum BidStatus {

    ONPROGRESS("sellBid_onprogress"),
    END("id_end");

    private final String value;
}