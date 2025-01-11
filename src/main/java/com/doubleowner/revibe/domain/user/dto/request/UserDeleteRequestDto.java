package com.doubleowner.revibe.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UserDeleteRequestDto {

    private final String password;

    public UserDeleteRequestDto(@JsonProperty("password") String password) {
        this.password = password;
    }
}
