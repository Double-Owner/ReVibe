package com.doubleowner.revibe.domain.cart.repository;

import com.doubleowner.revibe.domain.cart.entity.Cart;
import com.doubleowner.revibe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c JOIN FETCH c.option o JOIN FETCH o.item i JOIN FETCH c.user u WHERE c.user = :loginUser")
    List<Cart> findAllByUser(User loginUser);

    Cart findByIdAndUser(Long cartId, User loginUser);

    boolean existsByUserIdAndOptionId(Long id, Long itemId);
}
