package org.likelion.hsu.mini_hsu.Dto;

import lombok.Getter;
import org.likelion.hsu.mini_hsu.Entity.Movie;

@Getter
public class MovieResponseDto {
    private Long movieId;
    private String title;
    private String content;

    public MovieResponseDto(Movie movie) {
        this.movieId = movie.getId();
        this.title = movie.getTitle();
        this.content = movie.getContent();
    }
}
