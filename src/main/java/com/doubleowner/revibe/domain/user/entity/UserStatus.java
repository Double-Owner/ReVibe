package com.doubleowner.revibe.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatus {

    USER_ACTIVE("ACTIVE"),
    USER_DELETED("DELETED");

    private final String value;
}