package com.doubleowner.revibe.domain.item.repository;

import com.doubleowner.revibe.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    default Item findByIdOrElseThrow(Long itemId){
        return findById(itemId).orElseThrow(()->new RuntimeException("아이템을 찾을 수 없습니다."));
    }

}
