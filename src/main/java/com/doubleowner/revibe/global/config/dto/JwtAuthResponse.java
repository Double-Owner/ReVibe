package com.doubleowner.revibe.global.config.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class JwtAuthResponse {

    private String tokenAuthScheme;
    private String accessToken;
    private String refreshToken;

    public JwtAuthResponse(String tokenAuthScheme, String accessToken, String refreshToken) {
        this.tokenAuthScheme = tokenAuthScheme;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;

    }
}
