package com.doubleowner.revibe.domain.brand.repository;

import com.doubleowner.revibe.domain.brand.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    default Brand findByIdOrElseThrow(Long brandId) {
        return findById(brandId).orElseThrow();
    }
}