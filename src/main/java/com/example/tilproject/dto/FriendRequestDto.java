package com.example.tilproject.dto;

import com.example.tilproject.entity.Friendship;
import com.example.tilproject.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class FriendRequestDto {
    private Long friendshipId;
    private Long fromUserId;
    private String fromUsername;
    private Long toUserId;
    private String toUsername;
    private String status;

    public FriendRequestDto(Friendship friendship) {
        this.friendshipId = friendship.getFriendship_id();
        this.fromUserId = friendship.getFromUser().getUserId();
        this.fromUsername = friendship.getFromUser().getUsername();
        this.toUserId = friendship.getToUser().getUserId();
        this.toUsername = friendship.getToUser().getUsername();
        this.status = friendship.getStatus().name();
    }

}
