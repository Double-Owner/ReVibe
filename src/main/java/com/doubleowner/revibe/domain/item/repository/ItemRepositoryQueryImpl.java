package com.doubleowner.revibe.domain.item.repository;

import com.doubleowner.revibe.domain.brand.entity.QBrand;
import com.doubleowner.revibe.domain.item.entity.Item;
import com.doubleowner.revibe.domain.item.entity.QItem;
import com.doubleowner.revibe.domain.user.entity.QUser;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public class ItemRepositoryQueryImpl implements ItemRepositoryQuery {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Slice<Item> searchItems(Pageable pageable, String keyword, String brandName) {
        JPAQuery<Item> query = new JPAQuery<>(entityManager);

        QItem item = QItem.item;
        QBrand brand = QBrand.brand;
        QUser user = QUser.user;

        query.select(item)
                .from(item)
                .leftJoin(item.user, user).fetchJoin()
                .leftJoin(item.brand, brand).fetchJoin();

        if (keyword != null && !keyword.isEmpty()) {
            query.where(item.name.likeIgnoreCase("%" + keyword + "%"));
        }
        if (brandName != null) {
            query.where(item.brand.name.eq(brandName));
        }

        query.offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        return new PageImpl<>(query.fetch(), pageable, query.fetchCount());
    }
}
