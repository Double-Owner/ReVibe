package com.doubleowner.revibe.domain.brand.controller;

import com.doubleowner.revibe.domain.brand.dto.BrandRequestDto;
import com.doubleowner.revibe.domain.brand.dto.BrandResponseDto;
import com.doubleowner.revibe.domain.brand.service.BrandService;
import com.doubleowner.revibe.global.common.dto.CommonResponseBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    // 브랜드 등록
    @PostMapping
    public ResponseEntity<CommonResponseBody<BrandResponseDto>> createBrand(
            @Valid @RequestBody BrandRequestDto requestDto)
    {
        BrandResponseDto responseDto = brandService.createBrand(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponseBody<>("브랜드를 등록했습니다",responseDto));
    }

}
