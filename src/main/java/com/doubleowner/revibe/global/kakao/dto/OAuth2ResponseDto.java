package com.doubleowner.revibe.global.kakao.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OAuth2ResponseDto {

    private final String nickname;
    private final String email;

}
