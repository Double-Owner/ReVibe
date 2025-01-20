package com.doubleowner.revibe.domain.account.repository;

import com.doubleowner.revibe.domain.account.entity.Account;
import com.doubleowner.revibe.global.exception.CommonException;
import com.doubleowner.revibe.global.exception.errorCode.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<Account, Long> {
    default Account findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_VALUE,"해당 아이디의 계좌를 찾을 수 없습니다."));
    }

    @Modifying
    @Query("DELETE FROM Account WHERE id = :accountId")
    void deleteAccount(Long accountId);
}
