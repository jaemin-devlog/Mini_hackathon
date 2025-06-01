package com.example.running.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "badges")
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    // 이 뱃지를 주기 위한 최소 좋아요 수 기준
    @Column(nullable = false)
    private int requiredLikes;

    // 양방향 관계 - 이 뱃지를 가진 사용자들
    @ManyToMany(mappedBy = "badges")
    private List<User> users = new ArrayList<>();

    public Badge() {}

}
