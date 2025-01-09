package com.doubleowner.revibe.domain.user.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UserSignupResponseDto {

    private final Long userId;
    private final String nickName;
    private final String email;
    private final String profileImage;
    private final String address;
    private final String phoneNumber;
    private final LocalDateTime createdAt;

}
