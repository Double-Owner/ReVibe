package com.doubleowner.revibe.global.kakao.service;

import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.domain.user.repository.UserRepository;
import com.doubleowner.revibe.global.kakao.dto.KOAuthResponseDto;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserService {

    @Value("${KAKAO_CLIENT_ID}")
    private String client_id;

    @Value("${KAKAO_REDIRECT_URI}")
    private String redirect_uri;

    private final UserRepository userRepository;

    /**
     * 카카오 서버에서 생성된 accessToken 가져오는 과정
     * 1. 카카오에서 발급된 토큰
     */
    public String generateAccessToken(String code) {

        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com/oauth/token")
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", client_id)
                .queryParam("redirect_uri", redirect_uri)
                .queryParam("code", code)
                .encode()
                .build().toUri();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        return parsingTokenData(response);
    }

    /**
     * 3. 사용자 정보 가져오기
     * @param accessToken
     */
    public KOAuthResponseDto getUserInfo(String accessToken) {

        URI builder = UriComponentsBuilder.fromUriString("https://kapi.kakao.com/v2/user/me")
                .build().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(accessToken);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate
                .exchange(builder, HttpMethod.GET, new HttpEntity<>(null, headers), String.class);

        Map<String, String> stringObjectMap = parsingUserInfo(response);

        User user = new User(
                stringObjectMap.get("nickname"),
                stringObjectMap.get("email")
        );

        userRepository.save(user);

        return new KOAuthResponseDto(
                user.getNickname(),
                user.getEmail()
        );
    }

    /**
     * 4. 사용자 정보 파싱
     */
    private Map<String,String> parsingUserInfo(ResponseEntity<String> response) {
        JSONParser parser = new JSONParser();
        Map<String, String> userInfo = new HashMap<>();

        try {
            JSONObject jsonObject = (JSONObject) parser.parse(response.getBody());

            JSONObject properties = (JSONObject) jsonObject.get("properties");
            String nickname = (String) properties.get("nickname");

            JSONObject kakaoAccount = (JSONObject) jsonObject.get("kakao_account");
            String email = (String) kakaoAccount.get("email");

            userInfo.put("nickname", nickname);
            userInfo.put("email", email);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return userInfo;
    }

    // 2. 토큰 데이터 파싱
    private String parsingTokenData(ResponseEntity<String> response) {
        JSONParser parser = new JSONParser();
        String accessToken = "";
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(response.getBody());

            accessToken = (String) jsonObject.get("access_token");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return accessToken;
    }
}

