package com.example.running.repository;

import com.example.running.entity.Scrap;
import com.example.running.entity.User;
import com.example.running.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    // 특정 사용자와 게시글에 대한 스크랩 존재 여부 조회
    Optional<Scrap> findByUserAndPost(User user, Post post);

    // 게시글별 스크랩 개수 조회
    int countByPost(Post post);

    // 특정 사용자가 스크랩한 모든 게시글 목록 조회
    List<Scrap> findAllByUser(User user);
}
