package com.doubleowner.revibe.global.kakao.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KOAuthResponseDto {

    private final String nickname;
    private final String email;

}
