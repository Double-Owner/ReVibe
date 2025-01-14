package com.doubleowner.revibe.global.kakao.controller;

import com.doubleowner.revibe.global.kakao.service.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login/kakao")
public class KOAuthController {

    private final OAuth2UserService oAuth2UserService;

    /**
     * 카카오 소셜 로그인 요청 API
     * https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=562892494e966ea0729fe09e0402ea22&redirect_uri=http://localhost:8080/login/kakao
     * @param code
     */
    @GetMapping
    public void generateKakaoToken(@RequestParam String code) {
        String accessToken = oAuth2UserService.generateAccessToken(code);

        oAuth2UserService.getUserInfo(accessToken);
    }
}