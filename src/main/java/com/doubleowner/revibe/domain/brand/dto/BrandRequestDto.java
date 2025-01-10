package com.doubleowner.revibe.domain.brand.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BrandRequestDto {
    @NotBlank(message = "브랜드명 입력은 필수입니다.")
    private String name;
}
