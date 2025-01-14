package com.doubleowner.revibe.global.kakao.controller;

import com.doubleowner.revibe.global.kakao.service.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login/kakao")
public class KOAuthController {

    private final OAuth2UserService oAuth2UserService;

    @GetMapping
    public void generateKakaoToken(@RequestParam String code) {
        oAuth2UserService.generateAccessToken(code);

    }
}