package com.doubleowner.revibe.domain.cart.dto.response;

import com.doubleowner.revibe.domain.cart.entity.Cart;
import lombok.Getter;

@Getter
public class CartResponseDto {

    private final Long cartId;

    private final Long optionId;

    private final Long userId;

    public CartResponseDto(Long cartId,Long optionId, Long userId) {
        this.cartId = cartId;
        this.optionId = optionId;
        this.userId = userId;
    }

    public static CartResponseDto toDto(Cart cart) {
        return new CartResponseDto(
                cart.getId(),
                cart.getOption().getId(),
                cart.getUser().getId()
        );
    }

}
