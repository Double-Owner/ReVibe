package com.doubleowner.revibe.global.kakao.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class OAuth2UserService {

    @Value("${KAKAO_CLIENT_ID}")
    private String client_id;

    @Value("${KAKAO_REDIRECT_URI}")
    private String redirect_uri;

    /**
     * 카카오 서버에서 생성된 accessToken 가져오는 과정
     */
    public String generateAccessToken(String code) {

        String accessToken = "";

        try {
            URL url = new URL("https://kauth.kakao.com/oauth/token");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); //프론트 구현 고려
            httpURLConnection.setDoOutput(true);

            String requestBody = "grant_type=authorization_code"
                    + "&client_id=" + client_id
                    + "&redirect_uri=" + redirect_uri
                    + "&code=" + code;

            OutputStream os = httpURLConnection.getOutputStream();
            os.write(requestBody.getBytes(StandardCharsets.UTF_8));
            os.flush();
            os.close();

            InputStream responseStream = httpURLConnection.getResponseCode() == 200
                    ? httpURLConnection.getInputStream()
                    : httpURLConnection.getErrorStream();

            Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            System.out.println(jsonObject.toString());

            reader.close();
            responseStream.close();

            accessToken = (String) jsonObject.get("access_token");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return accessToken;
    }
}

