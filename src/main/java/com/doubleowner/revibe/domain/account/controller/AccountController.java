package com.doubleowner.revibe.domain.account.controller;

import com.doubleowner.revibe.domain.account.dto.AccountRequestDto;
import com.doubleowner.revibe.domain.account.dto.AccountResponseDto;
import com.doubleowner.revibe.domain.account.service.AccountService;
import com.doubleowner.revibe.global.common.dto.CommonResponseBody;
import com.doubleowner.revibe.global.config.auth.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<CommonResponseBody<AccountResponseDto>> createAccount(
            @Valid @RequestBody AccountRequestDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        AccountResponseDto accountResponseDto = accountService.create(userDetails.getUser(), dto);

        return new ResponseEntity<>(new CommonResponseBody<>("계좌 등록이 완료되었습니다.", accountResponseDto), HttpStatus.CREATED);

    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponseBody<AccountResponseDto>> findAccount(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        AccountResponseDto accountResponseDto = accountService.findAccount(userDetails.getUser(), id);

        return new ResponseEntity<>(new CommonResponseBody<>("나의 계좌 조회를 성공하였습니다.", accountResponseDto), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CommonResponseBody<AccountResponseDto>> updateAccount(
            @PathVariable Long id, @RequestBody AccountRequestDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        AccountResponseDto accountResponseDto = accountService.updateAccount(id, userDetails.getUser(), dto);

        return new ResponseEntity<>(new CommonResponseBody<>("나의 계좌 정보를 수정하였습니다.", accountResponseDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponseBody<Void>> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        accountService.deleteAccount(userDetails.getUser());

        return new ResponseEntity<>(new CommonResponseBody<>("나의 계좌 삭제가 완료되었습니다."), HttpStatus.OK);
    }
}
