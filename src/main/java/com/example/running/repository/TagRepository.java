package com.example.running.repository;

import com.example.running.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);  // 태그 이름으로 조회 (중복 방지용)
}
