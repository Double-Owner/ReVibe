package com.doubleowner.revibe.domain.item.repository;

import com.doubleowner.revibe.domain.item.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryQuery {

    Page<Item> searchItems(Pageable pageable,String keyword, String brand);
}
