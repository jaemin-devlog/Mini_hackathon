package com.example.running.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.example.running.dto.CommentRequestDto;
import com.example.running.entity.Comment;
import com.example.running.entity.Memo;
import com.example.running.repository.CommentRepository;
import com.example.running.repository.MemoRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemoRepository memoRepository;

    public Comment createComment(Long memoId, CommentRequestDto request) {
        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메모 아이디: " + memoId));
        Comment comment = new Comment(request.getContent(), memo);

        return commentRepository.save(comment);
    }

    public Comment updateComment(Long commentId, CommentRequestDto request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글 아이디: " + commentId));
        comment.setContent(request.getContent());
        return commentRepository.save(comment);
    }

    public  void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
