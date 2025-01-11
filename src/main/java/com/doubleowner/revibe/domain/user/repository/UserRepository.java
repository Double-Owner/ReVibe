package com.doubleowner.revibe.domain.user.repository;

import com.doubleowner.revibe.domain.account.entity.Account;
import com.doubleowner.revibe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.account = :account where u.id=:id")
    void updateUserByAccount(@Param("id") Long id, @Param("account") Account account);
}
