package com.example.running.dto;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class ScrapResponseDto {

    private boolean scrapped;
    private int scrapCount;

    public ScrapResponseDto(boolean scrapped, int scrapCount) {
        this.scrapped = scrapped;
        this.scrapCount = scrapCount;
    }

}
