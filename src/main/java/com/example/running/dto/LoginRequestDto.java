package com.example.running.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import com.example.running.entity.User;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequestDto {
    private String username;
    private String password;

    public User toEntity() {
        return User.builder()
                .username(username)
                .password(password)
                .build();
    }
}
