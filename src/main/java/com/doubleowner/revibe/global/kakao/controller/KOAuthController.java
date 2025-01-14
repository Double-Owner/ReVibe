package com.doubleowner.revibe.global.kakao.controller;

import com.doubleowner.revibe.domain.user.service.UserService;
import com.doubleowner.revibe.global.kakao.service.OAuth2UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login/kakao")
public class KOAuthController {

    private final OAuth2UserService oAuth2UserService;
    private final UserService userService;

    /**
     * 카카오 소셜 로그인 요청 API
     * https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=562892494e966ea0729fe09e0402ea22&redirect_uri=http://localhost:8080/login/kakao
     * @param code
     */

    @GetMapping
    public void generateKakaoToken(@RequestParam String code) {
        oAuth2UserService.generateAccessToken(code);
    }

//    @GetMapping("/login")
//    public ResponseEntity<String> login(@RequestParam String code, HttpServletRequest request) {
//
//        try {
//            // 토큰 발급
//            String accessToken = oAuth2UserService.generateAccessToken(code);
//
//            // 사용자 정보 조회
//            HashMap<String, Object> userInfo = oAuth2UserService.generateAccessToken(accessToken);
//
//            User user = new User(String)
//
//        }
//    }
}