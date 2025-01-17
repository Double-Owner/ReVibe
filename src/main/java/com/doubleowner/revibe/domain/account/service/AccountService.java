package com.doubleowner.revibe.domain.account.service;

import com.doubleowner.revibe.domain.account.dto.AccountRequestDto;
import com.doubleowner.revibe.domain.account.dto.AccountResponseDto;
import com.doubleowner.revibe.domain.account.entity.Account;
import com.doubleowner.revibe.domain.account.repository.AccountRepository;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.domain.user.repository.UserRepository;
import com.doubleowner.revibe.global.exception.AccountException;
import com.doubleowner.revibe.global.exception.errorCode.AccountErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Transactional
    public AccountResponseDto create(User user,AccountRequestDto dto) {
        Account account = Account.builder()
                .bank(dto.getBank())
                .account(dto.getAccount())
                .build();

        Account savedAccount = accountRepository.save(account);
        userRepository.updateUserByAccount(user.getId(),savedAccount);

        return AccountResponseDto.toDto(savedAccount);
    }

    @Transactional
    public AccountResponseDto findAccount(User user, Long id) {
        User findUserAccount = userRepository.findById(user.getId())
                .orElseThrow(() -> new AccountException(AccountErrorCode.ACCOUNT_NOT_FOUND));

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountException(AccountErrorCode.ACCOUNT_NOT_FOUND));

        if(!findUserAccount.getAccount().equals(account)){
            throw new AccountException(AccountErrorCode.INVALID_ACCOUNT_NUMBER);
        }

        return AccountResponseDto.toDto(account);

    }

    @Transactional
    public AccountResponseDto updateAccount(Long id ,User user, AccountRequestDto requestDto) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountException(AccountErrorCode.ACCOUNT_NOT_FOUND));

        account.update(requestDto);
        return AccountResponseDto.toDto(account);
    }

    @Transactional
    public void deleteAccount(User user) {
        User finduser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new AccountException(AccountErrorCode.REJECT_ACCOUNT_DELETE));

        finduser.deletedAccount();
    }
}
