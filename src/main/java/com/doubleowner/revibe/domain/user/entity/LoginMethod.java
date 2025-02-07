package com.doubleowner.revibe.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LoginMethod {

    LOCAL("LOCAL"),
    KAKAO("KAKAO");

    private final String value;
}