package com.example.running.repository;

import com.example.running.entity.Like;
import com.example.running.entity.Post;
import com.example.running.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserAndPost(User user, Post post);

    int countByPost(Post post);
}
