package com.example.running.service;

import com.example.running.dto.ScrapPostDto;
import com.example.running.dto.ScrapResponseDto;
import com.example.running.entity.Post;
import com.example.running.entity.Scrap;
import com.example.running.entity.User;
import com.example.running.repository.LikeRepository;
import com.example.running.repository.PostRepository;
import com.example.running.repository.ScrapRepository;
import com.example.running.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    public ScrapService(ScrapRepository scrapRepository, UserRepository userRepository, PostRepository postRepository,LikeRepository likeRepository) {
        this.scrapRepository = scrapRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
    }

    @Transactional
    public ScrapResponseDto toggleScrap(Long userId, Long postId) {
        // 1. 유저와 게시글 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        // 2. 기존 스크랩 여부 확인
        Optional<Scrap> existingScrap = scrapRepository.findByUserAndPost(user, post);

        boolean scrapped;
        if (existingScrap.isPresent()) {
            // 이미 스크랩한 상태면 취소 (삭제)
            scrapRepository.delete(existingScrap.get());
            scrapped = false;
        } else {
            // 스크랩 없으면 새로 저장
            Scrap scrap = new Scrap(user, post);
            scrapRepository.save(scrap);
            scrapped = true;
        }

        // 3. 게시글별 총 스크랩 수 조회
        int scrapCount = scrapRepository.countByPost(post);

        // 4. 결과 DTO 반환
        return new ScrapResponseDto(scrapped, scrapCount);
    }
    @Transactional(readOnly = true)
    public List<ScrapPostDto> getScrapList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<Scrap> scraps = scrapRepository.findAllByUser(user);

        return scraps.stream()
                .map(scrap -> {
                    Post post = scrap.getPost();
                    int likeCount = likeRepository.countByPost(post);
                    int scrapCount = scrapRepository.countByPost(post);

                    return new ScrapPostDto(
                            post.getId(),
                            post.getTitle(),
                            post.getUser().getUsername(),
                            likeCount,
                            scrapCount
                    );
                })
                .collect(Collectors.toList());
    }
}
