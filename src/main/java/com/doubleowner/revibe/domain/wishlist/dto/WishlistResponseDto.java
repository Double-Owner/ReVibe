package com.doubleowner.revibe.domain.wishlist.dto;

import com.doubleowner.revibe.domain.wishlist.entity.Wishlist;
import lombok.Getter;

@Getter
public class WishlistResponseDto {
    private final Long wishlistId;

    private final Long itemId;

    public WishlistResponseDto(Long wishlistId, Long itemId) {
        this.wishlistId = wishlistId;
        this.itemId = itemId;
    }

    public static WishlistResponseDto toDto(Wishlist wishlist) {
        return new WishlistResponseDto(
                wishlist.getId(),
                wishlist.getItem().getId()
        );
    }
}
