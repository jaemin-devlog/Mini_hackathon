package com.example.tilproject.entity;


import com.example.tilproject.enums.FriendshipStatus;
import com.example.tilproject.enums.TeamRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Friendship_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    private User toUser;

    //친구 요청 상태
    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;

}
