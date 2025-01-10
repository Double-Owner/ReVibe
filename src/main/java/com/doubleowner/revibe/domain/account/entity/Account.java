package com.doubleowner.revibe.domain.account.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String bank;

    @Column(nullable = false)
    private String account;
}
