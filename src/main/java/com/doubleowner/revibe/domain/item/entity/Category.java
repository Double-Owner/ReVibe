package com.doubleowner.revibe.domain.item.entity;

import com.doubleowner.revibe.global.exception.CommonException;
import com.doubleowner.revibe.global.exception.errorCode.ErrorCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Category {

    SLIPPER("slipper"),
    SNEAKERS("sneakers"),
    TRAINERS("trainers"),
    DRESS_SHOES("dressShoes"),
    BOOTS("boots");
    
    private final String categoryName;

    public static Category of(String categoryName){

        for (Category category : values()) {
            if (category.name().equals(categoryName)) {
                return category;
            }
        }
        throw new CommonException(ErrorCode.ILLEGAL_ARGUMENT,"잘못된 카테고리 이름입니다.");
    }
}