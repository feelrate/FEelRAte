package com.feelrate.controller;

import com.feelrate.domain.User;
import com.feelrate.dto.NaverUserInfo;
import com.feelrate.service.NaverOAuthService;
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
    private final NaverOAuthService naverOAuthService;
    private final UserService userService;

    @PostMapping("/naver")
    public ResponseEntity<?> loginWithNaver(@RequestBody Map<String, String> body) {
        String accessToken = body.get("accessToken");

        NaverUserInfo userInfo = naverOAuthService.getUserInfo(accessToken);
        User user = userService.loginOrRegister(userInfo);

        String jwt = "임시토큰"; // 나중에 JWT 생성기로 대체
        return ResponseEntity.ok(Map.of("token", jwt));
    }
}
