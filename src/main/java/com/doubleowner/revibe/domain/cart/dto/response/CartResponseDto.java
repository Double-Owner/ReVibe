package com.doubleowner.revibe.domain.cart.dto.response;

import com.doubleowner.revibe.domain.option.entity.Option;
import com.doubleowner.revibe.domain.option.entity.Size;
import lombok.Getter;

@Getter
public class CartResponseDto {

    private final Long itemId;

    private final Long optionId;

    private final String itemName;

    private final int size;

    public CartResponseDto(Long itemId,Long optionId, String itemName, int size) {
        this.itemId = itemId;
        this.optionId = optionId;
        this.itemName = itemName;
        this.size = size;
    }

    public static CartResponseDto toDto(Option option) {
        return new CartResponseDto(
                option.getId(),
                option.getItem().getId(),
                option.getItem().getName(),
                Size.toValue(option.getSize())
        );
    }

}
