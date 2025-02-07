package com.doubleowner.revibe.domain.like.repository;

import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.domain.like.entity.Like;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    @EntityGraph(attributePaths = {"user", "item"})
    boolean existsByUserIdAndItemId(Long userId, Long itemId);

    @EntityGraph(attributePaths = {"item"})
    List<Like> findByUser(User loginUser);


    @Query("SELECT l FROM Like l WHERE l.item.id NOT IN :itemIds")
    List<Like> findAllNotInItemIds(List<Long> itemIds);
}
