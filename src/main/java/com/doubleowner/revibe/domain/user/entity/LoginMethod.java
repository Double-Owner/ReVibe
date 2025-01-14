package com.doubleowner.revibe.domain.user.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum LoginMethod {

    LOCAL("LOCAL"),
    KAKAO("KAKAO");

    private final String value;
}