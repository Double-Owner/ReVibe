package com.doubleowner.revibe.global.kakao.service;

import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.domain.user.repository.UserRepository;
import com.doubleowner.revibe.global.config.dto.JwtAuthResponse;
import com.doubleowner.revibe.global.util.AuthenticationScheme;
import com.doubleowner.revibe.global.util.JwtProvider;
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
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OAuth2UserService {

    @Value("${kakao.client.id}")
    private String client_id;

    @Value("${kakao.redirect-url}")
    private String redirect_uri;

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

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
    public JwtAuthResponse getUserInfo(String accessToken) {

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

        String email = stringObjectMap.get("email");

        /**
         * DB에 정보가 없는 경우
         * null인 경우 처리 -> 한번도 카카오 로그인을 시도해본적이 없는 사람 (=최초 카카오 로그인 사용자) && 일반 로그인의 회원가입도 하지 않은 경우
         * existUser가 있는지 확인하고 (null인 경우) 회원가입
         */
        User existUser = userRepository.findByEmail(email).orElse(null);
        if (Objects.isNull(existUser)) {
            return login(userRepository.save(user));
        } else {
            return login(existUser);
        }
    }

    /**
     * 로그인
     */
    public JwtAuthResponse login(User existUser) {

        Map<String, String> generatedTokens = jwtProvider.generateTokens(existUser.getEmail(), existUser.getRole());
        String generatedAccessToken = generatedTokens.get("access_token");

        return new JwtAuthResponse(AuthenticationScheme.BEARER.getName(), generatedAccessToken);
    }

    /**
     * 2. 토큰 데이터 파싱
     */
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

    /**
     * 4. 사용자 정보 파싱
     */
    private Map<String,String> parsingUserInfo(ResponseEntity<String> response) {
        JSONParser parser = new JSONParser();
        Map<String, String> userInfo = new HashMap<>();

        try {
            JSONObject jsonObject = (JSONObject) parser.parse(response.getBody());

            System.out.println(jsonObject);

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
}

