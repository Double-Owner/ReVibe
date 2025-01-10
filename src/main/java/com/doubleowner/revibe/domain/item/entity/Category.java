package com.doubleowner.revibe.domain.item.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Category {

    SLIPPER("slipper"),
    SNEAKERS("sneakers"),
    TRAINERS("trainers"),
    DRESS_SHOES("dressShoes"),
    BOOTS("boots");
    
    private final String categoryName;

    public static Category of(String categoryName) throws IllegalArgumentException {

        for (Category category : values()) {
            if (category.name().equals(categoryName)) {
                return category;
            }
        }

        throw new IllegalArgumentException("잘못된 카테고리이름입니다. : " + categoryName);
    }
}