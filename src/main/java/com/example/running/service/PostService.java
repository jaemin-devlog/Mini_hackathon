package com.example.running.service;

import com.example.running.dto.PostResponseDto;
import com.example.running.dto.PostUpdateRequestDto;
import com.example.running.entity.Post;
import com.example.running.entity.PostImage;
import com.example.running.entity.Tag;
import com.example.running.entity.User;
import com.example.running.repository.PostImageRepository;
import com.example.running.repository.PostRepository;
import com.example.running.repository.TagRepository;
import com.example.running.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostImageRepository postImageRepository;
    private final TagRepository tagRepository;
    private final FileStorageService fileStorageService;

    // 내 게시글 조회
    @Transactional(readOnly = true)
    public List<PostResponseDto> getPostsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        List<Post> posts = postRepository.findAllByUser(user);

        return posts.stream()
                .map(post -> new PostResponseDto(
                        post.getPostId(),
                        post.getTitle(),
                        post.getContent(),
                        post.getCreatedAt().toString(),
                        post.getImages().stream().map(PostImage::getImageUrl).collect(Collectors.toList()),
                        post.getTags().stream().map(Tag::getName).collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    // 게시글 작성
    @Transactional
    public PostResponseDto createPost(String title, String content, List<String> tagNames,
                                      MultipartFile[] images, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        post.setUser(user);

        // 태그 처리
        handleTags(post, tagNames);

        Post savedPost = postRepository.save(post);

        // 이미지 처리
        List<String> imageUrls = handleImages(savedPost, images);

        return new PostResponseDto(
                savedPost.getPostId(),
                savedPost.getTitle(),
                savedPost.getContent(),
                savedPost.getCreatedAt().toString(),
                imageUrls,
                tagNames != null ? tagNames : new ArrayList<>()
        );
    }

    // 게시글 수정
    @Transactional
    public PostResponseDto updatePost(Long postId, PostUpdateRequestDto updateRequestDto, MultipartFile[] images) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        post.setTitle(updateRequestDto.getTitle());
        post.setContent(updateRequestDto.getContent());
        post.setUpdatedAt(LocalDateTime.now());

        // 태그 처리
        handleTags(post, Collections.singletonList(updateRequestDto.getTags()));

        // 이미지 처리
        List<String> imageUrls = handleImages(post, images);

        postRepository.save(post);

        return new PostResponseDto(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt().toString(),
                imageUrls,
                updateRequestDto.getTags() != null ? Collections.singletonList(updateRequestDto.getTags()) : new ArrayList<>()
        );
    }

    // 태그명으로 게시글 검색
    @Transactional(readOnly = true)
    public List<PostResponseDto> getPostsByTagName(String tagName) {
        List<Post> posts = postRepository.findAllByTags_Name(tagName);

        return posts.stream()
                .map(post -> new PostResponseDto(
                        post.getPostId(),
                        post.getTitle(),
                        post.getContent(),
                        post.getCreatedAt().toString(),
                        post.getImages().stream().map(PostImage::getImageUrl).collect(Collectors.toList()),
                        post.getTags().stream().map(Tag::getName).collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        // 연관된 이미지도 함께 삭제됨 (orphanRemoval = true)
        postRepository.delete(post);
    }

    // 태그 처리 공통 메서드
    private void handleTags(Post post, List<String> tagNames) {
        // 기존 태그 제거
        List<Tag> existingTags = new ArrayList<>(post.getTags());
        for (Tag tag : existingTags) {
            post.removeTag(tag);
        }

        // 새로운 태그 추가
        if (tagNames != null) {
            for (String tagName : tagNames) {
                Tag tag = tagRepository.findByName(tagName)
                        .orElseGet(() -> {
                            Tag newTag = new Tag();
                            newTag.setName(tagName);
                            return tagRepository.save(newTag);
                        });
                post.addTag(tag);
            }
        }
    }

    // 이미지 처리 공통 메서드
    private List<String> handleImages(Post post, MultipartFile[] images) {
        // 기존 이미지 삭제
        List<PostImage> existingImages = new ArrayList<>(post.getImages());
        for (PostImage image : existingImages) {
            post.removeImage(image);
            postImageRepository.delete(image);
        }

        List<String> imageUrls = new ArrayList<>();
        if (images != null) {
            for (MultipartFile image : images) {
                try {
                    String imageUrl = fileStorageService.uploadFile(image);
                    PostImage postImage = new PostImage();
                    postImage.setPost(post);
                    postImage.setImageUrl(imageUrl);
                    postImageRepository.save(postImage);
                    imageUrls.add(imageUrl);
                } catch (Exception e) {
                    throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다.", e);
                }
            }
        }
        return imageUrls;
    }
}
