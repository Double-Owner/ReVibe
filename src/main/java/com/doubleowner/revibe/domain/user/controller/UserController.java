package com.doubleowner.revibe.domain.user.controller;

import com.doubleowner.revibe.domain.user.dto.request.UserSignupRequestDto;
import com.doubleowner.revibe.domain.user.dto.response.UserSignupResponseDto;
import com.doubleowner.revibe.domain.user.service.UserService;
import com.doubleowner.revibe.global.common.dto.CommonResponseBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * 회원가입
     * @param requestDto - 회원가입을 위한 요청 정보
     * @return UserSignupResponseDto - 회원가입 완료 응답 dto
     */
    @PostMapping("/signup")
    public ResponseEntity<CommonResponseBody<UserSignupResponseDto>> signUp(
            @Valid @RequestBody UserSignupRequestDto requestDto) {
        UserSignupResponseDto response = userService.signUpUser(requestDto);
        return new ResponseEntity<>(new CommonResponseBody<>("회원가입이 완료되었습니다.", response), HttpStatus.OK);

    }

}
