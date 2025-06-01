package com.example.tilproject.dto;

import com.example.tilproject.entity.User;
import com.example.tilproject.enums.TeamRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class LoginRequestDto {
    private String username;
    private String password;
    private TeamRole role;

    public User toEntity() {
        return User.builder()
                .username(username)
                .password(password)
                .role(role)
                .build();
    }
}
