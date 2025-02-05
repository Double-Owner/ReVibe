package com.doubleowner.revibe.domain.item.entity;

import com.doubleowner.revibe.global.exception.CustomException;
import com.doubleowner.revibe.global.exception.errorCode.ErrorCode;
import lombok.RequiredArgsConstructor;

import static com.doubleowner.revibe.global.exception.errorCode.ErrorCode.ILLEGAL_ARGUMENT;

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
        throw new CustomException(ILLEGAL_ARGUMENT);
    }
}