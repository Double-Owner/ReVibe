package com.doubleowner.revibe.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    ADMIN("admin"),
    USER("user");

    private final String value;
}