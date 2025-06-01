package com.example.running.repository;

import com.example.running.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BadgeRepository extends JpaRepository<Badge, Long> {

    // 좋아요 기준으로 뱃지 찾기 (예: 좋아요 수 이하 중 가장 큰 requiredLikes)
    Optional<Badge> findTopByRequiredLikesLessThanEqualOrderByRequiredLikesDesc(int likes);
}
