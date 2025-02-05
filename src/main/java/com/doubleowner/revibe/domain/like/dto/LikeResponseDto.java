package com.doubleowner.revibe.domain.like.dto;

import com.doubleowner.revibe.domain.like.entity.Like;
import lombok.Getter;

@Getter
public class LikeResponseDto {

    private final Long itemId;

    public LikeResponseDto(Long itemId) {
        this.itemId = itemId;
    }

    public static LikeResponseDto toDto(Like like) {
        return new LikeResponseDto(
                like.getItem().getId()
        );
    }
}
