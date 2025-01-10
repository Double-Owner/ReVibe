package com.doubleowner.revibe.domain.item.dto.response;

import com.doubleowner.revibe.domain.item.entity.Category;
import com.doubleowner.revibe.domain.item.entity.Item;
import lombok.Getter;

@Getter
public class ItemResponseDto {

    private Long itemId;

    private String name;

    private String description;

    private Category category;

    private String image;

    private Long brandId;

    private Long userId;

    public ItemResponseDto(Long itemId, String name, String description, Category category, String image, Long brandId, Long userId) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.image = image;
        this.brandId = brandId;
        this.userId = userId;
    }

    public static ItemResponseDto toDto(Item item) {
        return new ItemResponseDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getCategory(),
                item.getImage(),
                item.getBrand().getId(),
                item.getUser().getId()
        );
    }

}