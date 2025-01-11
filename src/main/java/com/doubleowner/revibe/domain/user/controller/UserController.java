package com.doubleowner.revibe.domain.user.controller;

import com.doubleowner.revibe.domain.user.dto.request.UserDeleteRequestDto;
import com.doubleowner.revibe.domain.user.dto.request.UserLoginRequestDto;
import com.doubleowner.revibe.domain.user.dto.request.UserSignupRequestDto;
import com.doubleowner.revibe.domain.user.dto.response.UserSignupResponseDto;
import com.doubleowner.revibe.domain.user.service.UserService;
import com.doubleowner.revibe.global.common.dto.CommonResponseBody;
import com.doubleowner.revibe.global.config.auth.UserDetailsImpl;
import com.doubleowner.revibe.global.config.dto.JwtAuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * 회원가입
     *
     * @param requestDto - 회원가입을 위한 요청 정보
     * @return UserSignupResponseDto - 회원가입 완료 응답 dto
     */
    @PostMapping("/signup")
    public ResponseEntity<CommonResponseBody<UserSignupResponseDto>> signUp(
            @Valid @RequestBody UserSignupRequestDto requestDto) {
        UserSignupResponseDto response = userService.signUpUser(requestDto);
        return new ResponseEntity<>(new CommonResponseBody<>("회원가입이 완료되었습니다.", response), HttpStatus.CREATED);

    }

    /**
     * 로그인
     *
     * @param requestDto 로그인 요청 정보
     * @return JWT 토큰 응답
     */
    @PostMapping("/login")
    public ResponseEntity<CommonResponseBody<JwtAuthResponse>> login(
            @Valid @RequestBody UserLoginRequestDto requestDto) {

        JwtAuthResponse authResponse = this.userService.login(requestDto);

        return new ResponseEntity<>(new CommonResponseBody<>("로그인을 성공했습니다.", authResponse), HttpStatus.OK);
    }

    /**
     * 회원 삭제
     */
    @DeleteMapping()
    public ResponseEntity<CommonResponseBody<Void>> deleteUser(
            @RequestBody UserDeleteRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            Authentication authentication) {

        userService.deleteUser(userDetails.getUsername(),requestDto);

        if (authentication == null && !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("회원을 삭제할 수 없습니다.");
        }
        new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, null);

        return new ResponseEntity<>(new CommonResponseBody<>("회원삭제가 완료되었습니다."), HttpStatus.OK);
    }
            /**
             * 프로필 수정
             */

            /**
             * 프로필 조회
             */


}