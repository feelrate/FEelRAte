package com.feelrate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NaverUserInfo {
    private String id;
    private String email;
    private String nickname;
}