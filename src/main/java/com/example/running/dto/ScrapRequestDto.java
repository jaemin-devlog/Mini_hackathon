package com.example.running.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ScrapRequestDto {

    private Long userId;
    private Long postId;

    public ScrapRequestDto(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
    }
}
