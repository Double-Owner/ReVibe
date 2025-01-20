package com.doubleowner.revibe.domain.wishList.entity;

import com.doubleowner.revibe.domain.item.entity.Item;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class WishList extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemId")
    private Item item;

    public WishList(User user, Item item) {
        this.user = user;
        this.item = item;
    }

}
