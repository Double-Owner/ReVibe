package com.doubleowner.revibe.domain.user.dto.response;

import com.doubleowner.revibe.domain.user.entity.UserStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UserProfileResponseDto {

    private final Long userId;
    private final String nickName;
    private final String email;
    private final String profileImage;
    private final String address;
    private final String phoneNumber;
    private final UserStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}
