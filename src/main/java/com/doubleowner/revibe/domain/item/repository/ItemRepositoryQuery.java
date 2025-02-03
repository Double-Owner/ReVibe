package com.doubleowner.revibe.domain.item.repository;

import com.doubleowner.revibe.domain.item.entity.Item;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ItemRepositoryQuery {

    Slice<Item> searchItems(Pageable pageable, String keyword, String brand);
}
