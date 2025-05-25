package com.example.running.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.example.running.entity.Comment;
import com.example.running.entity.Memo;

@Setter
@Getter
@NoArgsConstructor
public class CommentRequestDto {
    private String content;

    public Comment toEntity() {
        return Comment.builder()
                .content(content)
                .build();
    }
}
