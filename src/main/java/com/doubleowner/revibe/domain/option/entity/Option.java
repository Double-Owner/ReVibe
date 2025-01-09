package com.doubleowner.revibe.domain.option.entity;

import com.doubleowner.revibe.domain.item.entity.Item;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private Size size;

    //상품 옵션 사이즈 별 갯수
    @Column(nullable = false)
    private Long stock;

    @OneToMany
    private List<Item> items;
}
