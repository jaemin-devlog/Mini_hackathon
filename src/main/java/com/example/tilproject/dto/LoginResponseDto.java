package com.example.tilproject.dto;

import com.example.tilproject.entity.User;
import com.example.tilproject.enums.TeamRole;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
    private Long userId;
    private String username;
    private String accessToken;
    private TeamRole role;

    public static LoginResponseDto of(Long userId, String username, String accessToken, TeamRole role) {
        return LoginResponseDto.builder()
                .userId(userId)
                .username(username)
                .role(role)
                .accessToken(accessToken)
                .build();
    }
}
