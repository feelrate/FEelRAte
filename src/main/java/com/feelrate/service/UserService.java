package com.feelrate.service;

import com.feelrate.domain.User;
import com.feelrate.dto.KakaoUserInfo;
import com.feelrate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User loginOrRegister(KakaoUserInfo info) {
        return userRepository.findByKakaoId(info.getId())
                .orElseGet(() -> {
                    User user = new User();
                    user.setKakaoId(info.getId());
                    user.setEmail(info.getEmail());
                    user.setNickname(info.getNickname());
                    user.setCreatedAt(LocalDateTime.now());
                    return userRepository.save(user);
                });
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));
    }
}