package com.example.running.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostRequestDto {
    private String title;
    private String content;
    private List<String> tags;
    private Long userId;
}
