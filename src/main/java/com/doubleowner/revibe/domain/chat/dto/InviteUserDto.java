package com.doubleowner.revibe.domain.chat.dto;

import lombok.Getter;

@Getter
public class InviteUserDto {

    private Long roomId;

    private String email; // 초대할 사람 이메일
}
