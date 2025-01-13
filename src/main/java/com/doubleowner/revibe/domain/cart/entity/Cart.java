package com.doubleowner.revibe.domain.cart.entity;

import com.doubleowner.revibe.domain.item.entity.Item;
import com.doubleowner.revibe.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Item item;

    @ManyToOne
    private User user;

    public Cart(User user, Item item) {
        this.user = user;
        this.item = item;
    }
}