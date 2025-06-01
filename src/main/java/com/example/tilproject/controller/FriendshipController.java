package com.example.tilproject.controller;

import com.example.tilproject.security.UserPrincipal;
import com.example.tilproject.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friendships")
@RequiredArgsConstructor
public class FriendshipController {
    private final FriendshipService friendshipService;

    //친구 요청 보내기
    @PostMapping("/send/{toUserId}")
    public ResponseEntity<?> sendFriendship(@PathVariable Long toUserId, @AuthenticationPrincipal UserPrincipal userPrincipal) {

        friendshipService.sendFriendsRequest(userPrincipal.getUserId(), toUserId);
        return ResponseEntity.ok("친구 요청을 보냈습니다.");
    }
    // 친구 요청 수락
    @PostMapping("/accept/{fromUserId}")
    public ResponseEntity<?> acceptFriendRequest(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                 @PathVariable Long fromUserId) {
        friendshipService.acceptFriendRequest(userPrincipal.getUserId(), fromUserId);
        return ResponseEntity.ok("친구 요청을 수락했습니다.");
    }
    // 친구 목록 조회
    @GetMapping("/list")
    public ResponseEntity<?> getFriendList(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(friendshipService.getFriendList(userPrincipal.getUserId()));
    }
}
