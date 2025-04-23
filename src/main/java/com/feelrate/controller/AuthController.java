package com.feelrate.controller;

import com.feelrate.domain.User;
import com.feelrate.dto.KakaoUserInfo;
import com.feelrate.security.JwtProvider;
import com.feelrate.service.KakaoOAuthService;
import com.feelrate.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final KakaoOAuthService kakaoOAuthService;
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @PostMapping("/kakao")
    public ResponseEntity<?> loginWithKakao(@RequestBody Map<String, String> body) {
        String accessToken = body.get("accessToken");
        KakaoUserInfo userInfo = kakaoOAuthService.getUserInfo(accessToken);
        User user = userService.loginOrRegister(userInfo);
        String jwt = jwtProvider.createToken(user.getId());

        return ResponseEntity.ok(Map.of("token", jwt));
    }
}