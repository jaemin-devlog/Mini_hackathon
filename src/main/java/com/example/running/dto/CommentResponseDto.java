package com.example.running.dto;

import lombok.Getter;
import lombok.Setter;
import com.example.running.entity.Comment;
import com.example.running.entity.Memo;

@Getter
@Setter
public class CommentResponseDto {
    private Long commentId;
    private Long memoId;
    private String content;

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.memoId = comment.getMemo().getMemoId();
        this.content = comment.getContent();
    }
}