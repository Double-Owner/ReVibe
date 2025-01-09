package com.doubleowner.revibe.domain.item.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Category {

    SLIPPER("slipper"),
    SNEAKERS("sneakers"),
    TRAINERS("trainers"),
    DRESS_SHOES("dressShoes"),
    BOOTS("boots");
    
    private final String value;
}