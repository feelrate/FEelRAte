package com.feelrate.service;

import com.feelrate.dto.KakaoUserInfo;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class KakaoOAuthService {

    public KakaoUserInfo getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                entity,
                Map.class
        );

        Map<String, Object> body = response.getBody();
        Map<String, Object> kakaoAccount = (Map<String, Object>) body.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String kakaoId = String.valueOf(body.get("id"));
        String nickname = (String) profile.get("nickname");

        // ✅ email이 없으면 기본값으로 대체
        String email = (String) kakaoAccount.get("email");
        if (email == null || email.isBlank()) {
            email = kakaoId + "@kakao.local";
        }

        return new KakaoUserInfo(
                kakaoId,
                email,
                nickname
        );
    }
}