package com.doubleowner.revibe.domain.brand.service;

import com.doubleowner.revibe.domain.brand.dto.BrandRequestDto;
import com.doubleowner.revibe.domain.brand.dto.BrandResponseDto;
import com.doubleowner.revibe.domain.brand.entity.Brand;
import com.doubleowner.revibe.domain.brand.repository.BrandRepository;
import com.doubleowner.revibe.global.exception.CustomException;
import com.doubleowner.revibe.global.exception.errorCode.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    // 브랜드 등록
    public BrandResponseDto createBrand(BrandRequestDto requestDto) {
        Brand brand = new Brand(requestDto.getName());
       if(brandRepository.existsByName(requestDto.getName())){
           throw new CustomException(ErrorCode.ALREADY_EXIST);
       }

        brandRepository.save(brand);

        return BrandResponseDto.toDto(brand);
    }

}
