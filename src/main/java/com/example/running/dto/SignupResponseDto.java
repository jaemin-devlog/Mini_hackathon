package com.example.running.dto;

import lombok.Getter;

@Getter
public class SignupResponseDto {
    private final Long userId;
    private final String username;
    private final String nickname;

    public SignupResponseDto(Long userId, String username, String nickname) {
        this.userId = userId;
        this.username = username;
        this.nickname = nickname;
    }
}
