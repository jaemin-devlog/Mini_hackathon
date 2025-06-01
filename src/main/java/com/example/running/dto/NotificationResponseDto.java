package com.example.running.dto;

import com.example.running.entity.Notification;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NotificationResponseDto {
    private final String content;
    private final Long postId;
    private final LocalDateTime createdAt;

    public NotificationResponseDto(Notification notification) {
        this.content = notification.getContent();
        this.postId = notification.getPost().getPostId();
        this.createdAt = notification.getCreatedAt();
    }
}
