package com.doubleowner.revibe.domain.user.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserLoginResponseDto {

    private final Long userId;

    private final String accessToken;

}
