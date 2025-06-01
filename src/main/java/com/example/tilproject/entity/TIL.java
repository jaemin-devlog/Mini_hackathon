package com.example.tilproject.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class TIL {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long TILId;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public TIL(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

}
