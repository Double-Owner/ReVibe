package com.doubleowner.revibe.domain.user.dto.request;

import com.doubleowner.revibe.domain.user.entity.UserStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@RequiredArgsConstructor
public class UserSignupRequestDto {

    @NotBlank(message = "닉네임은 필수 값입니다.")
    private final String nickname;

    @NotBlank(message = "이메일은 필수 값입니다.")
    private final String email;

    @NotBlank(message = "비밀번호는 필수 값입니다")
    private final String password;

    private final MultipartFile profileImage;
    private final String address;
    private final String phoneNumber;
    private final UserStatus status;

    @NotBlank(message = "권한 설정은 필수 값입니다")
    private final String role;

}
