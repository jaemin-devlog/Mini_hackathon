package com.example.running.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BadgeResponse {

    private Long id;
    private String name;
    private int requiredLikes;

    public BadgeResponse(Long id, String name, int requiredLikes) {
        this.id = id;
        this.name = name;
        this.requiredLikes = requiredLikes;
    }
}
