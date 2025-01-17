package com.doubleowner.revibe.domain.cart.entity;

import com.doubleowner.revibe.domain.option.entity.Option;
import com.doubleowner.revibe.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Option option;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Cart(User user, Option option) {
        this.user = user;
        this.option = option;
    }
}