package com.example.running.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LikeResponseDto {
    private boolean liked;
    private int likeCount;

    public LikeResponseDto(boolean liked, int likeCount) {
        this.liked = liked;
        this.likeCount = likeCount;
    }
}
