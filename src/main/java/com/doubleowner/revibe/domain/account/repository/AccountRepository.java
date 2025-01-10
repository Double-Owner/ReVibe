package com.doubleowner.revibe.domain.account.repository;

import com.doubleowner.revibe.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
