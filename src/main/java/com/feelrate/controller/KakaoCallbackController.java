package com.feelrate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/login/oauth2/code")
public class KakaoCallbackController {

    @GetMapping("/kakao")
    @ResponseBody
    public String kakaoCallback(@RequestParam String code) {
        return "CODE: " + code;
    }
}