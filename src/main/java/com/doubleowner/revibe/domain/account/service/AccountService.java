package com.doubleowner.revibe.domain.account.service;

import com.doubleowner.revibe.domain.account.dto.AccountRequestDto;
import com.doubleowner.revibe.domain.account.dto.AccountResponseDto;
import com.doubleowner.revibe.domain.account.entity.Account;
import com.doubleowner.revibe.domain.account.repository.AccountRepository;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.domain.user.repository.UserRepository;
import com.doubleowner.revibe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.doubleowner.revibe.global.exception.errorCode.ErrorCode.*;

@Service
@RequiredArgsConstructor
@DynamicUpdate
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Transactional
    public AccountResponseDto create(User user, AccountRequestDto dto) {
        if (user.getAccount() != null) {
            throw new CustomException(INVALID_ACCOUNT);
        }

        Account account = Account.builder()
                .bank(dto.getBank())
                .account(dto.getAccount())
                .build();

        Account savedAccount = accountRepository.save(account);

        userRepository.updateUserByAccount(user, savedAccount);

        return AccountResponseDto.toDto(savedAccount);
    }

    @Transactional
    public AccountResponseDto findAccount(User user) {

        if (user.getAccount() == null) {
            throw new CustomException(NOT_FOUND_ACCOUNT);
        }
        return AccountResponseDto.toDto(user.getAccount());
    }

    @Transactional
    public AccountResponseDto updateAccount(User user, AccountRequestDto requestDto) {

        if (user.getAccount() == null) {
            throw new CustomException(NOT_FOUND_ACCOUNT);
        }

        user.getAccount().update(requestDto);
        accountRepository.save(user.getAccount());
        return AccountResponseDto.toDto(user.getAccount());
    }

    @Transactional
    public void deleteAccount(User user) {
        if (user.getAccount() == null) {
            throw new CustomException(NOT_FOUND_ACCOUNT);
        }
        userRepository.updateUserByAccount(user, null);
        accountRepository.delete(user.getAccount());

    }
}
