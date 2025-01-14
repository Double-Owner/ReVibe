package com.doubleowner.revibe.domain.user.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@RequiredArgsConstructor
public class UserProfileUpdateRequestDto {

    private final String password;
    private final String nickname;
    private final MultipartFile profileImage;
    private final String address;
    private final String phoneNumber;
}
