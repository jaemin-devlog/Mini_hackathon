package com.example.tilproject.dto;

import com.example.tilproject.entity.TIL;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TILResponseDto {
    private Long tilId;
    private String title;
    private String content;

    public TILResponseDto(TIL til) {
        this.tilId = til.getTILId();
        this.title = til.getTitle();
        this.content = til.getContent();
    }
}