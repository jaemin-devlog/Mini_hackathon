package com.example.running.service;

import com.example.running.dto.NotificationResponseDto;
import com.example.running.entity.Notification;
import com.example.running.entity.Post;
import com.example.running.entity.User;
import com.example.running.repository.NotificationRepository;
import com.example.running.sse.SseEmitterManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SseEmitterManager emitterManager;

    public void notifyComment(User receiver, User sender, Post post) {
        // 본인이 본인 글에 댓글 단 경우 예외 처리
        if (receiver.getUserId().equals(sender.getUserId())) return;

        Notification notification = Notification.builder()
                .receiver(receiver)
                .content("누군가 당신의 게시글에 댓글을 남겼어요!")
                .post(post)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);

        // 실시간 전송
        emitterManager.send(receiver.getUserId(), new NotificationResponseDto(notification));
    }

    public List<NotificationResponseDto> getNotifications(User user) {
        return notificationRepository.findByReceiverOrderByCreatedAtDesc(user).stream()
                .map(NotificationResponseDto::new)
                .toList();
    }
}
