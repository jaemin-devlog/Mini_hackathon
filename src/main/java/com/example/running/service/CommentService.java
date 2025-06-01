package com.example.running.service;

import com.example.running.dto.CommentRequestDto;
import com.example.running.entity.Comment;
import com.example.running.entity.Post;
import com.example.running.entity.User;
import com.example.running.repository.CommentRepository;
import com.example.running.repository.PostRepository;
import com.example.running.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public Comment createComment(Long postId, Long userId, CommentRequestDto request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글 아이디: " + postId));

        User sender = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 아이디: " + userId));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .post(post)
                .user(sender)
                .build();
        Comment savedComment = commentRepository.save(comment);

        // 알림 전송 (자신의 글에 댓글 단 경우 제외됨)
        notificationService.notifyComment(post.getUser(), sender, post, request.getContent());
        return savedComment;
    }

    public Comment updateComment(Long commentId, CommentRequestDto request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글 아이디: " + commentId));
        comment.setContent(request.getContent());
        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
