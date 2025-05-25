package com.example.running.dto;

import lombok.Getter;

@Getter
public class ScrapPostDto {
    // Getters
    private Long postId;
    private String title;
    private String author;
    private int likeCount;
    private int scrapCount;

    public ScrapPostDto(Long postId, String title, String author, int likeCount, int scrapCount) {
        this.postId = postId;
        this.title = title;
        this.author = author;
        this.likeCount = likeCount;
        this.scrapCount = scrapCount;
    }

}
