package com.doubleowner.revibe.domain.user.dto.request;

import com.doubleowner.revibe.domain.user.entity.UserStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@RequiredArgsConstructor
public class UserSignupRequestDto {

    private final String nickname;
    private final String email;
    private final String password;
    private final MultipartFile profileImage;
    private final String address;
    private final String phoneNumber;
    private final UserStatus status;
    private final String role;
}
