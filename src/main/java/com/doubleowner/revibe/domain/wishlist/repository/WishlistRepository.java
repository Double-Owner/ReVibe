package com.doubleowner.revibe.domain.wishlist.repository;

import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.domain.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    @EntityGraph(attributePaths = {"user", "item"})
    boolean existsByUserIdAndItemId(Long userId, Long itemId);

    @EntityGraph(attributePaths = {"user", "item"})
    void deleteByUserIdAndItemId(Long userId, Long itemId);

    List<Wishlist> findByUser(User loginUser);
}
