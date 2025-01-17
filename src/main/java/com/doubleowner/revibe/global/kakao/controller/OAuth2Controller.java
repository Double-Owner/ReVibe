package com.doubleowner.revibe.global.kakao.controller;

import com.doubleowner.revibe.global.common.dto.CommonResponseBody;
import com.doubleowner.revibe.global.config.dto.JwtAuthResponse;
import com.doubleowner.revibe.global.kakao.service.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login/kakao")
public class OAuth2Controller {

    private final OAuth2UserService oAuth2UserService;

    /**
     * 카카오 소셜 로그인 요청 API
     * @param code
     */
    @GetMapping
    public ResponseEntity<CommonResponseBody<JwtAuthResponse>> generateKakaoToken(@RequestParam String code) {
        String accessToken = oAuth2UserService.generateAccessToken(code);

        JwtAuthResponse userInfo = oAuth2UserService.getUserInfo(accessToken);

        return new ResponseEntity<>(new CommonResponseBody<>("로그인을 성공했습니다.", userInfo), HttpStatus.OK);
    }
}