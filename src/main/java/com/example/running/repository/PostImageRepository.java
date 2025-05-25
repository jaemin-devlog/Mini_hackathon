package com.example.running.repository;

import com.example.running.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    List<PostImage> findAllByPostId(Long postId);  // 특정 게시글의 이미지 목록 조회용
}
