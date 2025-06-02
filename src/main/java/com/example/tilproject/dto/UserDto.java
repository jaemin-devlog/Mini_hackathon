package com.example.tilproject.dto;

import lombok.Data;
import lombok.Getter;

@Data
public class UserDto {
    private Long id;          // userId
    private String username;  // username
    private String role;      // 필요시

    public UserDto(Long id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }
    // getter/setter
}

