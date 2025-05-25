package com.example.running.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostRequestDto {
    private String title;
    private String content;
    private List<String> imageUrls;  // 클라이언트에서 업로드 후 이미지 주소를 전달
    private List<String> tags;       // 태그 이름 리스트
}
