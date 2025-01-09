package com.doubleowner.revibe.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_USER("ROLE_USER");

    private final String name;

    public static Role of(String roleName) throws IllegalArgumentException {
        for (Role role : values()) {
            if (role.getName().equals(roleName.toUpperCase())) {
                return role;
            }
        }
        throw new IllegalArgumentException("해당하는 이름의 권한을 찾을 수 없습니다: " + roleName);

    }
}