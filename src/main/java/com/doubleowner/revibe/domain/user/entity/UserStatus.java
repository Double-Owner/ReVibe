package com.doubleowner.revibe.domain.user.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserStatus {

    USER_ACTIVE("active"),
    USER_SLEEP("sleep"),
    USER_QUIT("quit");

    private final String value;
}