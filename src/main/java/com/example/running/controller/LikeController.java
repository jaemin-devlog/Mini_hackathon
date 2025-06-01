package com.example.running.controller;

import com.example.running.dto.LikeRequestDto;
import com.example.running.dto.LikeResponseDto;
import com.example.running.dto.PostLikeInfoDto;
import com.example.running.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    /**
     * 게시글 좋아요 토글 API (userId 직접 전달)
     */
    @PostMapping("/like")
    public ResponseEntity<LikeResponseDto> toggleLike(
            @RequestParam Long userId,
            @RequestBody LikeRequestDto likeRequest) {

        LikeResponseDto response = likeService.toggleLike(userId, likeRequest.getPostId());
        return ResponseEntity.ok(response);
    }

    /**
     * 내가 받은 총 좋아요 수 조회 API
     */
    @GetMapping("/likes/total")
    public ResponseEntity<Integer> getTotalLikes(@RequestParam Long userId) {
        int totalLikes = likeService.getTotalLikes(userId);
        return ResponseEntity.ok(totalLikes);
    }

    /**
     * 내가 쓴 게시글별 좋아요 수 목록 조회 API
     */
    @GetMapping("/likes/posts")
    public ResponseEntity<List<PostLikeInfoDto>> getPostLikeInfos(@RequestParam Long userId) {
        List<PostLikeInfoDto> postLikeInfos = likeService.getMyPostLikeInfo(userId);
        return ResponseEntity.ok(postLikeInfos);
    }
}
