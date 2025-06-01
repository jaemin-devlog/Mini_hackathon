package com.example.tilproject.dto;

import com.example.tilproject.entity.TIL;
import com.example.tilproject.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TILRequestDto {
    private String title;
    private String content;

    public TIL toEntity(User user) {
        return TIL.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
    }
}
