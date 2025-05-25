package com.example.running.controller;

import com.example.running.dto.LikeRequestDto;
import com.example.running.dto.LikeResponseDto;
import com.example.running.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    /**
     * 게시글 좋아요 토글 API
     * @param likeRequest 요청 바디에 포함된 postId
     * @param userId 로그인한 사용자 ID (스프링 시큐리티에서 가져온다고 가정)
     * @return 좋아요 상태 및 좋아요 개수
     */
    @PostMapping("/like")
    public ResponseEntity<LikeResponseDto> toggleLike(
            @RequestBody LikeRequestDto likeRequest,
            @AuthenticationPrincipal(expression = "id") Long userId) {

        LikeResponseDto response = likeService.toggleLike(userId, likeRequest.getPostId());
        return ResponseEntity.ok(response);
    }
}
