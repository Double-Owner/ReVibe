package com.doubleowner.revibe.domain.brand.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
}
