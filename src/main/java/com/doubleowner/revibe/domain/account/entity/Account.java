package com.doubleowner.revibe.domain.account.entity;

import com.doubleowner.revibe.domain.account.dto.AccountRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String bank;

    @Column(nullable = false)
    private String account;

    public void update(AccountRequestDto dto) {
        this.bank= dto.getBank();
        this.account=dto.getAccount();
    }
}
