package com.example.running.service;

import com.example.running.dto.LikeResponseDto;
import com.example.running.dto.PostLikeInfoDto;
import com.example.running.entity.Like;
import com.example.running.entity.Post;
import com.example.running.entity.User;
import com.example.running.repository.LikeRepository;
import com.example.running.repository.PostRepository;
import com.example.running.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final BadgeService badgeService;

    public LikeService(LikeRepository likeRepository,
                       PostRepository postRepository,
                       UserRepository userRepository,
                       BadgeService badgeService) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.badgeService = badgeService;
    }

    @Transactional
    public LikeResponseDto toggleLike(Long userId, Long postId) {
        // 1. User, Post 엔티티 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        // 2. 기존 좋아요 기록 조회
        Optional<Like> existingLike = likeRepository.findByUserAndPost(user, post);

        boolean liked;
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            liked = false;
        } else {
            Like like = new Like();
            like.setUser(user);
            like.setPost(post);
            likeRepository.save(like);
            liked = true;
        }

        // 3. 좋아요 개수 조회
        int likeCount = likeRepository.countByPost(post);

        // 4. 사용자 총 좋아요 수 계산
        int totalLikes = calculateUserTotalLikes(user);

        // 5. 뱃지 부여 확인
        badgeService.checkAndAssignBadge(user, totalLikes);

        // 6. 결과 응답 DTO 반환
        return new LikeResponseDto(liked, likeCount);
    }

    // 유저가 작성한 게시글들의 총 좋아요 수 계산
    private int calculateUserTotalLikes(User user) {
        return user.getPosts().stream()
                .mapToInt(likeRepository::countByPost)
                .sum();
    }

    // 내가 받은 총 좋아요 수 조회 (서비스 메서드)
    public int getTotalLikes(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));
        return calculateUserTotalLikes(user);
    }

    // 내가 쓴 게시글별 좋아요 수 목록 조회 (서비스 메서드)
    public List<PostLikeInfoDto> getMyPostLikeInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));

        return user.getPosts().stream()
                .map(post -> new PostLikeInfoDto(
                        post.getPostId(),
                        post.getTitle(),
                        likeRepository.countByPost(post)
                ))
                .collect(Collectors.toList());
    }
}
