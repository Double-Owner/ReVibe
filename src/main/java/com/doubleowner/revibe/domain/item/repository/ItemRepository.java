package com.doubleowner.revibe.domain.item.repository;

import com.doubleowner.revibe.domain.item.entity.Item;
import com.doubleowner.revibe.global.exception.CustomException;
import com.doubleowner.revibe.global.exception.errorCode.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import static com.doubleowner.revibe.global.exception.errorCode.ErrorCode.NOT_FOUND_VALUE;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryQuery {

    default Item findByIdOrElseThrow(Long itemId){
        return findById(itemId).orElseThrow(()->new CustomException(NOT_FOUND_VALUE));
    }

    boolean existsByName(String name);
}
