package com.doubleowner.revibe.domain.item.repository;

import com.doubleowner.revibe.domain.item.entity.Item;
import com.doubleowner.revibe.global.exception.CommonException;
import com.doubleowner.revibe.global.exception.errorCode.ErrorCode;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    default Item findByIdOrElseThrow(Long itemId){
        return findById(itemId).orElseThrow(()->new CommonException(ErrorCode.NOT_FOUND_VALUE,"해당 상품을 찾을 수 없습니다."));
    }

    boolean existsByName(String name);
}
