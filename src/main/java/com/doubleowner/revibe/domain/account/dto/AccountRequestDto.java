package com.doubleowner.revibe.domain.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AccountRequestDto {

    @NotBlank(message = "은행 이름은 필수 값입니다")
    private String bank;

    @NotNull(message = "계좌 번호는 필수 값입니다")
    @NotBlank(message = "계좌 번호는 공백일 수 없습니다.")
    private String account;

}
