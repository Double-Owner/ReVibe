package com.doubleowner.revibe.domain.account.dto;

import com.doubleowner.revibe.domain.account.entity.Account;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponseDto {

    @NotNull(message = "Account ID는 필수 값입니다")
    private Long AccountId;

    @NotBlank(message = "은행 이름은 필수 값입니다")
    private String bank;

    @NotNull(message = "계좌 번호는 필수 값입니다")
    @NotBlank(message = "계좌 번호는 공백일 수 없습니다.")
    private String account;

    public static AccountResponseDto toDto(Account account) {
        return new AccountResponseDto(
                account.getId(),
                account.getBank(),
                account.getAccount()
        );
    }
}
