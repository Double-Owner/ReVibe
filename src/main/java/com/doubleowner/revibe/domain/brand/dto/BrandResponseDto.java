package com.doubleowner.revibe.domain.brand.dto;

import com.doubleowner.revibe.domain.brand.entity.Brand;
import lombok.Getter;

@Getter
public class BrandResponseDto {

    private Long brandId;

    private String name;

    public BrandResponseDto(Long brandId, String name) {
        this.brandId = brandId;
        this.name = name;
    }

    public static BrandResponseDto toDto(Brand brand) {
        return new BrandResponseDto(
                brand.getId(),
                brand.getName());
    }
}
