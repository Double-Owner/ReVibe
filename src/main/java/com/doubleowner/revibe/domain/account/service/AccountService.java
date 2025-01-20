package com.doubleowner.revibe.domain.account.service;

import com.doubleowner.revibe.domain.account.dto.AccountRequestDto;
import com.doubleowner.revibe.domain.account.dto.AccountResponseDto;
import com.doubleowner.revibe.domain.account.entity.Account;
import com.doubleowner.revibe.domain.account.repository.AccountRepository;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.domain.user.repository.UserRepository;
import com.doubleowner.revibe.global.exception.CommonException;
import com.doubleowner.revibe.global.exception.errorCode.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@DynamicUpdate
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Transactional
    public AccountResponseDto create(User user,AccountRequestDto dto) {
        if(user.getAccount() != null){
            throw new CommonException(ErrorCode.ALREADY_EXIST,"계좌가 이미 등록되어 있습니다.");
        }

        Account account = Account.builder()
                .bank(dto.getBank())
                .account(dto.getAccount())
                .build();

        Account savedAccount = accountRepository.save(account);

        userRepository.updateUserByAccount(user,savedAccount);

        return AccountResponseDto.toDto(savedAccount);
    }

    @Transactional
    public AccountResponseDto findAccount(User user) {

        if(user.getAccount() == null){
            throw new CommonException(ErrorCode.NOT_FOUND_VALUE,"로그인 중인 사용자의 계좌가 없습니다.");
        }
        return AccountResponseDto.toDto(user.getAccount());
    }

    @Transactional
    public AccountResponseDto updateAccount(User user, AccountRequestDto requestDto) {

        if(user.getAccount() == null){
            throw new CommonException(ErrorCode.NOT_FOUND_VALUE,"사용자의 계좌가 없습니다.");
        }

        user.getAccount().update(requestDto);
        accountRepository.save(user.getAccount());
        return AccountResponseDto.toDto(user.getAccount());
    }

    @Transactional
    public void deleteAccount(User user) {
        if(user.getAccount() == null){
            throw new CommonException(ErrorCode.NOT_FOUND_VALUE,"사용자의 계좌가 없습니다.");
        }
        userRepository.updateUserByAccount(user,null);
        accountRepository.delete(user.getAccount());

    }
}
