package org.likelion.hsu.mini_hsu.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //리뷰 제목
    @Column(nullable = false)
    private String title;

    //리뷰 내용
    @Column(nullable = false)
    private String content;

    private int likeCount = 0;  // 좋아요 개수 필드

}
