package com.example.running.dto;

import lombok.Getter;

@Getter
public class ScrapPostDto {
    // Getters
    private final Long postId;
    private final String title;
    private final String author;
    private final int likeCount;
    private final int scrapCount;

    public ScrapPostDto(Long postId, String title, String author, int likeCount, int scrapCount) {
        this.postId = postId;
        this.title = title;
        this.author = author;
        this.likeCount = likeCount;
        this.scrapCount = scrapCount;
    }

}
