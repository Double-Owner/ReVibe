package com.doubleowner.revibe.domain.wishList.dto;

import com.doubleowner.revibe.domain.wishList.entity.WishList;
import lombok.Getter;

@Getter
public class WishListResponseDto {
    private final Long wishListId;

    private final Long itemId;

    public WishListResponseDto(Long wishListId, Long itemId) {
        this.wishListId = wishListId;
        this.itemId = itemId;
    }

    public static WishListResponseDto toDto(WishList wishList) {
        return new WishListResponseDto(
                wishList.getId(),
                wishList.getItem().getId()
        );
    }
}
