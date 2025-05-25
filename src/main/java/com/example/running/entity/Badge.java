package com.example.running.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "badges")
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 뱃지 이름: 초보러너, 중수러너, 고수러너 등
    @Column(nullable = false, unique = true)
    private String name;

    // 이 뱃지를 주기 위한 최소 좋아요 수 기준
    @Column(nullable = false)
    private int requiredLikes;

    // 양방향 관계 - 이 뱃지를 가진 사용자들
    @ManyToMany(mappedBy = "badges")
    private List<User> users = new ArrayList<>();

    public Badge() {}

    // getters & setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRequiredLikes() {
        return requiredLikes;
    }

    public void setRequiredLikes(int requiredLikes) {
        this.requiredLikes = requiredLikes;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
