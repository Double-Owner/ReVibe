package com.doubleowner.revibe.domain.user.repository;

import com.doubleowner.revibe.domain.account.entity.Account;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.global.exception.CommonException;
import com.doubleowner.revibe.global.exception.errorCode.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    default User findByEmailOrElseThrow(String email) {
        return findByEmail(email).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_VALUE, "해당 이메일의 유저를 찾을 수 없습니다."));
    }

    default User findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_VALUE, "해당 아이디의 유저를 찾을 수 없습니다."));
    }

    @Modifying
    @Query("UPDATE User u SET u.account = :account where u =:user")
    void updateUserByAccount(User user, @Param("account") Account account);
}
