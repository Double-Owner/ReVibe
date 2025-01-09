package com.doubleowner.revibe.domain.user.entity;

import com.doubleowner.revibe.domain.account.Account;
import com.doubleowner.revibe.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String profileImage;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role Role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus Status;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;
}