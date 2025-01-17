package com.doubleowner.revibe.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserLoginRequestDto {

    @NotBlank(message = "이메일은 필수 값입니다.")
    private final String email;

    @NotBlank(message = "비밀번호는 필수 값입니다")
    private final String password;

}