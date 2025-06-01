package com.example.running.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostUpdateRequestDto {
    private Long postId;        // 수정할 게시글 ID
    private Long userId;        // 요청자 ID (권한 체크용)
    private String title;       // 수정할 제목
    private String content;     // 수정할 내용
    private String tags;        // 수정할 태그들 (콤마 등 구분자 포함 가능)
}
