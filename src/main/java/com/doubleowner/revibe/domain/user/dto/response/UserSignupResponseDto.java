package com.doubleowner.revibe.domain.user.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UserSignupResponseDto {

    @NotNull(message = "사용자 ID는 필수 값입니다.")
    private final Long userId;

    @NotBlank(message = "닉네임은 필수 값입니다.")
    private final String nickname;

    @NotBlank(message = "이메일은 필수 값입니다.")
    private final String email;

    private final String profileImage;
    private final String address;
    private final String phoneNumber;

    @PastOrPresent(message = "생성일은 과거나 현재 날짜여야 합니다.")
    private final LocalDateTime createdAt;

}
