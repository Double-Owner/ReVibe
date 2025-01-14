package com.doubleowner.revibe.global.kakao.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class OAuth2UserService {

    @Value("${KAKAO_CLIENT_ID}")
    private String client_id;

    @Value("${KAKAO_REDIRECT_URI}")
    private String redirect_uri;

//    private final RestTemplate restTemplate;

    /**
     * 카카오 서버에서 생성된 accessToken 가져오는 과정
     * 1. 카카오에서 발급된 토큰
     */
    public String generateAccessToken(String code) {

        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com/oauth/token")
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id",client_id)
                .queryParam("redirect_uri", redirect_uri)
                .queryParam("code", code)
                .encode()
                .build().toUri();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        return parsingData(response);
    }

    // 2. 데이터 파싱하기
    private String parsingData(ResponseEntity<String> response) {
        JSONParser parser = new JSONParser();
        String accessToken = "";
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(response.toString());

            accessToken = (String) jsonObject.get("access_token");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return accessToken;
    }


}

