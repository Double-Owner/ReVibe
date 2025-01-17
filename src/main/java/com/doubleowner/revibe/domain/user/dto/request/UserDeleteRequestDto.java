package com.doubleowner.revibe.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserDeleteRequestDto {

    @NotBlank(message = "비밀번호는 필수 값입니다")
    private final String password;

    public UserDeleteRequestDto(@JsonProperty("password") String password) {
        this.password = password;
    }
}
