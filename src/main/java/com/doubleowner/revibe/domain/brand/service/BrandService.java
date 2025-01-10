package com.doubleowner.revibe.domain.brand.service;

import com.doubleowner.revibe.domain.brand.dto.BrandRequestDto;
import com.doubleowner.revibe.domain.brand.dto.BrandResponseDto;
import com.doubleowner.revibe.domain.brand.entity.Brand;
import com.doubleowner.revibe.domain.brand.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    // 브랜드 등록
    public BrandResponseDto createBrand(BrandRequestDto requestDto) {
        Brand brand = new Brand(requestDto.getName());
        // TODO 이미 브랜드명이 존재 할 경우 예외처리
        brandRepository.save(brand);

        return BrandResponseDto.toDto(brand);
    }

}
