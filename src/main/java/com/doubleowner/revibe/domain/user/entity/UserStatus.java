package com.doubleowner.revibe.domain.user.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserStatus {

    USER_ACTIVE("ACTIVE"),
    USER_DELETED("DELETED");

    private final String value;
}