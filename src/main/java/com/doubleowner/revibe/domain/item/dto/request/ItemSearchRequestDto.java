package com.doubleowner.revibe.domain.item.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemSearchRequestDto {

    private String keyword;

    private String brand;
}
