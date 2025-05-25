package com.example.running.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostResponseDto {
    private Long postId;
    private String title;
    private String content;
    private String createdAt;  // 필요하면 날짜 형식 맞춰서 변환
    private List<String> imageUrls;  // 이미지 URL 리스트
    private List<String> tags;       // 태그 이름 리스트
}
