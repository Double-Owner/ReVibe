package com.doubleowner.revibe.domain.cart.repository;

import com.doubleowner.revibe.domain.cart.entity.Cart;
import com.doubleowner.revibe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findAllByUser(User loginUser);

    Cart findByIdAndUser(Long id, User loginUser);

    boolean existsByUserIdAndOptionId(Long id, Long optionId);
}
