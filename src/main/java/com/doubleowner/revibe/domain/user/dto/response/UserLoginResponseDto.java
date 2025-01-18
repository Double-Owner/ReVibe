package com.doubleowner.revibe.domain.user.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserLoginResponseDto {

    @NotNull(message = "사용자 ID는 필수 값입니다.")
    private final Long userId;

    private final String accessToken;

}
