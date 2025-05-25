package com.example.running.service;

import com.example.running.entity.Badge;
import com.example.running.entity.User;
import com.example.running.repository.BadgeRepository;
import com.example.running.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final UserRepository userRepository;

    public BadgeService(BadgeRepository badgeRepository, UserRepository userRepository) {
        this.badgeRepository = badgeRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void checkAndAssignBadge(User user, int totalLikes) {
        // 1. 조건에 맞는 최고 등급 뱃지 찾기
        Optional<Badge> badgeOpt = badgeRepository.findTopByRequiredLikesLessThanEqualOrderByRequiredLikesDesc(totalLikes);

        if (badgeOpt.isEmpty()) {
            return; // 조건 맞는 뱃지가 없으면 종료
        }

        Badge badge = badgeOpt.get();

        // 2. 이미 뱃지를 가지고 있으면 리턴
        if (user.getBadges().contains(badge)) {
            return;
        }

        // 3. 새 뱃지 추가
        user.getBadges().add(badge);
        userRepository.save(user);  // 뱃지 변경 저장
    }
}
