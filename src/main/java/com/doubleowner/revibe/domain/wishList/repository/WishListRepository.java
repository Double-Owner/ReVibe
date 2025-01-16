package com.doubleowner.revibe.domain.wishList.repository;

import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.domain.wishList.entity.WishList;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {
    @EntityGraph(attributePaths = {"user", "item"})
    boolean existsByUserIdAndItemId(Long userId, Long itemId);

    @EntityGraph(attributePaths = {"user", "item"})
    void deleteByUserIdAndItemId(Long userId, Long itemId);

    List<WishList> findByUser(User loginUser);
}
