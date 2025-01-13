package com.doubleowner.revibe.domain.cart.dto.response;

import com.doubleowner.revibe.domain.cart.entity.Cart;
import lombok.Getter;

@Getter
public class CartResponseDto {

    private final Long cartId;

    private final Long ItemId;

    private final Long userId;

    public CartResponseDto(Long cartId, Long ItemId, Long userId) {
        this.cartId = cartId;
        this.ItemId = ItemId;
        this.userId = userId;
    }

    public static CartResponseDto toDto(Cart cart) {
        return new CartResponseDto(
                cart.getId(),
                cart.getItem().getId(),
                cart.getUser().getId()
        );
    }

}
