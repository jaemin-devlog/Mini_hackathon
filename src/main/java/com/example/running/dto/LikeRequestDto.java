package com.example.running.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LikeRequestDto {
    private Long postId;

    public LikeRequestDto() {}

    public LikeRequestDto(Long postId) {
        this.postId = postId;
    }

}