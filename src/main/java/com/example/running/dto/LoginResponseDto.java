package com.example.running.dto;

import lombok.Getter;
import lombok.Setter;
import com.example.running.entity.User;

@Getter @Setter
public class LoginResponseDto {
    private String username;
    private String password;

    public LoginResponseDto(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
    }
}
