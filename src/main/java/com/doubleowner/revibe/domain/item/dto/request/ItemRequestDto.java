package com.doubleowner.revibe.domain.item.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemRequestDto {
    @NotNull(message = "브랜드 입력은 필수입니다")
    private Long brandId;

    @NotBlank(message = "카테고리 입력은 필수입니다")
    private String category;

    @NotBlank(message = "상품이름 입력은 필수입니다")
    private String name;

    @NotBlank(message = "설명 입력은 필수입니다")
    private String description;

    private String image;

}
