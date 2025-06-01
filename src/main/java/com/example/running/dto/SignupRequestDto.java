package com.example.running.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignupRequestDto {
    private String username;  // 로그인 아이디
    private String password;
    private String nickname;  // 닉네임
}
