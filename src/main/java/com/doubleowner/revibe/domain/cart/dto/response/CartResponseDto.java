package com.doubleowner.revibe.domain.cart.dto.response;

import com.doubleowner.revibe.domain.cart.entity.Cart;
import com.doubleowner.revibe.domain.option.entity.Size;
import lombok.Getter;

@Getter
public class CartResponseDto {

    private final Long cartId;

    private final String itemName;

    private final int size;

    private final Long userId;

    public CartResponseDto(Long cartId, String itemName, int size, Long userId) {
        this.cartId = cartId;
        this.itemName = itemName;
        this.size = size;
        this.userId = userId;
    }

    public static CartResponseDto toDto(Cart cart) {
        return new CartResponseDto(
                cart.getId(),
                cart.getOption().getItem().getName(),
                Size.toValue(cart.getOption().getSize()),
                cart.getUser().getId()
        );
    }

}
