package com.example.tilproject.service;

import com.example.tilproject.entity.Friendship;
import com.example.tilproject.entity.User;
import com.example.tilproject.enums.FriendshipStatus;
import com.example.tilproject.repository.FriendRepository;
import com.example.tilproject.repository.TILRepository;
import com.example.tilproject.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendshipService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    @Transactional
    public void sendFriendsRequest(Long fromUserId, Long toUserId) {
        if (fromUserId.equals(toUserId)) {
            throw new IllegalArgumentException("자기 자신의 친구 요청을 보낼 수 없습니다.");
        }

        User fromUser = userRepository.findById(fromUserId)
                .orElseThrow(() -> new IllegalArgumentException("보내는 유저가 존재하지 않습니다."));
        User toUser = userRepository.findById(toUserId)
                .orElseThrow(() -> new IllegalArgumentException("받는 유저가 존재하지 않습니다."));
        if (friendRepository.existsByFromUserAndToUser(fromUser, toUser)) {
            throw new IllegalStateException("이미 친구 요청을 보냈습니다.");
        }
        Friendship friendship = Friendship.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .status(FriendshipStatus.REQUESTED)
                .build();
        friendRepository.save(friendship);
    }

    //친구 요청 수락

    @Transactional
    public void acceptFriendRequest(Long fromUserId, Long toUserId) {
        Friendship friendship = friendRepository.findByUserIdsBidirectional(fromUserId, toUserId)
                .orElseThrow(() -> new IllegalArgumentException("해당 친구 요청이 존재하지 않습니다."));

        if (friendship.getStatus() != FriendshipStatus.REQUESTED) {
            throw new IllegalStateException("요청 상태가 아니므로 수락할 수 없습니다.");
        }

        friendship.setStatus(FriendshipStatus.ACCEPTED);
    }
    //친구 목록 조회
    public List<User> getFriendList(Long userId) {
        return friendRepository.findAcceptedFriends(userId);
    }

    //친구인지 확인
    public boolean isFriend(Long user1, Long user2) {
        return friendRepository.isFriend(user1, user2);
    }
}
