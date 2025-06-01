package org.likelion.hsu.mini_hsu.Dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieRequestDto {
    private Long movieId;
    private String title;
    private String content;
}
