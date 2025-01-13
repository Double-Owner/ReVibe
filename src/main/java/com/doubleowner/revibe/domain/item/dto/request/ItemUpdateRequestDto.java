package com.doubleowner.revibe.domain.item.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemUpdateRequestDto {
    private String name;

    private String category;

    private String description;

    private String image;
}
