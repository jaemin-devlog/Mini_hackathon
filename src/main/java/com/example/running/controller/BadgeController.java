package com.example.running.controller;

import com.example.running.dto.BadgeResponse;
import com.example.running.entity.User;
import com.example.running.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/badges")
public class BadgeController {

    private final UserRepository userRepository;

    public BadgeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/user/{userId}")
    @Transactional(readOnly = true)
    public ResponseEntity<List<BadgeResponse>> getUserBadges(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<BadgeResponse> badgeResponses = user.getBadges().stream()
                .map(badge -> new BadgeResponse(badge.getId(), badge.getName(), badge.getRequiredLikes()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(badgeResponses);
    }
    }

