package com.doubleowner.revibe.domain.account.service;

import com.doubleowner.revibe.domain.account.dto.AccountRequestDto;
import com.doubleowner.revibe.domain.account.dto.AccountResponseDto;
import com.doubleowner.revibe.domain.account.entity.Account;
import com.doubleowner.revibe.domain.account.repository.AccountRepository;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.domain.user.repository.UserRepository;
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
                .orElseThrow(() -> new RuntimeException("계좌 정보를 찾을 수 없습니다."));

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("계좌 정보를 찾을 수 없습니다."));

        if(!findUserAccount.getAccount().equals(account)){
            throw new RuntimeException("해당 계좌는 사용자의 계좌가 아닙니다");
        }

        return AccountResponseDto.toDto(account);

    }

    @Transactional
    public AccountResponseDto updateAccount(Long id ,User user, AccountRequestDto requestDto) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("계좌 정보를 찾을 수 없습니다."));

        account.update(requestDto);
        return AccountResponseDto.toDto(account);
    }

    @Transactional
    public void deleteAccount(User user) {
        User finduser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("계좌 삭제에 실패했습니다. ID를 다시 입력해주세요."));

        finduser.deleteCount();
    }
}
