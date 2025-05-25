package com.example.running.controller;

import com.example.running.dto.PostRequestDto;
import com.example.running.dto.PostResponseDto;
import com.example.running.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 기존 내 게시글 조회 API
    @GetMapping("/my-posts")
    public List<PostResponseDto> getMyPosts(@RequestParam Long userId) {
        return postService.getPostsByUserId(userId);
    }

    // 게시글 작성 API (글 + 사진 + 태그)
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam(required = false) List<String> tags,
            @RequestPart(required = false) MultipartFile[] images,
            @RequestParam Long userId) {  // 작성자 아이디

        PostResponseDto response = postService.createPost(title, content, tags, images, userId);
        return ResponseEntity.ok(response);
    }

    // 태그로 게시글 검색 API
    @GetMapping("/search-by-tag")
    public ResponseEntity<List<PostResponseDto>> getPostsByTag(@RequestParam String tagName) {
        List<PostResponseDto> posts = postService.getPostsByTagName(tagName);
        return ResponseEntity.ok(posts);
    }
}
