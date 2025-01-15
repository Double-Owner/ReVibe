package com.doubleowner.revibe.domain.brand.repository;

import com.doubleowner.revibe.domain.brand.entity.Brand;
import com.doubleowner.revibe.global.exception.CommonException;
import com.doubleowner.revibe.global.exception.errorCode.ErrorCode;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    default Brand findByIdOrElseThrow(Long brandId) {
        return findById(brandId).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_VALUE,"해당 브랜드를 찾을 수 없습니다."));
    }

    boolean existsByName(String name);
}