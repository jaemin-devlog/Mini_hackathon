package com.example.running.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostLikeInfoDto {
    private Long postId;     // 게시글 ID
    private String title;    // 게시글 제목
    private int likeCount;   // 게시글에 달린 좋아요 수
}
