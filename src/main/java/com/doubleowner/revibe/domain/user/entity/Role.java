package com.doubleowner.revibe.domain.user.entity;

import com.doubleowner.revibe.global.exception.CustomException;
import com.doubleowner.revibe.global.exception.errorCode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static com.doubleowner.revibe.global.exception.errorCode.ErrorCode.UNAUTHORIZED_ACCESS;

@Getter
@RequiredArgsConstructor
public enum Role {

    ROLE_ADMIN("ADMIN"),
    ROLE_USER("USER");

    private final String name;

    public static Role of(String roleName) {
        for (Role role : values()) {
            if (role.getName().equals(roleName.toUpperCase())) {
                return role;
            }
        }
        throw new CustomException(UNAUTHORIZED_ACCESS);
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.name()));
    }
}