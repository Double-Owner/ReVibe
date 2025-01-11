package com.doubleowner.revibe.domain.account.dto;

import com.doubleowner.revibe.domain.account.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponseDto {

    private Long AccountId;
    private String bank;
    private String account;

    public static AccountResponseDto toDto(Account account) {
        return new AccountResponseDto(
                account.getId(),
                account.getBank(),
                account.getAccount()
        );
    }
}
