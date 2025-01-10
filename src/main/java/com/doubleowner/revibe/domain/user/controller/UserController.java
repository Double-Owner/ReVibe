package com.doubleowner.revibe.domain.user.controller;

import com.doubleowner.revibe.domain.user.dto.request.UserLoginRequestDto;
import com.doubleowner.revibe.domain.user.dto.request.UserSignupRequestDto;
import com.doubleowner.revibe.domain.user.dto.response.UserSignupResponseDto;
import com.doubleowner.revibe.domain.user.service.UserService;
import com.doubleowner.revibe.global.common.dto.CommonResponseBody;
import com.doubleowner.revibe.global.config.dto.JwtAuthResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return new ResponseEntity<>(new CommonResponseBody<>("회원가입 완료", response), HttpStatus.OK);

    }

    /**
     * 로그인
     * @param requestDto 로그인 요청 정보
     * @return JWT 토큰 응답
     */
    @PostMapping("/login")
    public ResponseEntity<CommonResponseBody<JwtAuthResponse>> login(
            @Valid @RequestBody UserLoginRequestDto requestDto) {

        JwtAuthResponse authResponse = this.userService.login(requestDto);

        return ResponseEntity.ok(new CommonResponseBody<>("로그인 성공", authResponse));
    }

    /**
     * 회원 삭제
     */

    /**
     * 프로필 수정
     */

    /**
     * 프로필 조회
     */

}
