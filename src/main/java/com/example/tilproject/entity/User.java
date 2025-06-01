package com.example.tilproject.entity;

import com.example.tilproject.enums.TeamRole;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private TeamRole role;

    @OneToMany(mappedBy = "fromUser")
    private List<Friendship> sentRequests;

    @OneToMany(mappedBy = "toUser")
    private List<Friendship> receivedRequests;

    @Builder
    public User(Long userId, String username, String password, TeamRole role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
