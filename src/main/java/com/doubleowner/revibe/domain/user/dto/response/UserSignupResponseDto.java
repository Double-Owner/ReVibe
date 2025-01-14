package com.doubleowner.revibe.domain.user.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UserSignupResponseDto {

    private final Long userId;
    private final String nickname;
    private final String email;
    private final String profileImage;
    private final String address;
    private final String phoneNumber;
    private final LocalDateTime createdAt;

}
