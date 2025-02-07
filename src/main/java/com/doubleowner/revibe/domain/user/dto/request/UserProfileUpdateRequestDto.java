package com.doubleowner.revibe.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@RequiredArgsConstructor
public class UserProfileUpdateRequestDto {

    @NotBlank(message = "비밀번호는 필수 값입니다")
    private final String password;

    @NotBlank(message = "닉네임은 필수 값입니다.")
    private final String nickname;

    private final MultipartFile profileImage;
    private final String address;
    private final String phoneNumber;
}
