package com.example.running.service;

import com.example.running.dto.PostResponseDto;
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
                        post.getId(),
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
        List<Tag> tags = new ArrayList<>();
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

        Post savedPost = postRepository.save(post);

        // 이미지 처리
        List<String> imageUrls = new ArrayList<>();
        if (images != null) {
            for (MultipartFile image : images) {
                try {
                    String imageUrl = fileStorageService.uploadFile(image); // 여기 메서드명 변경에 맞게 수정
                    PostImage postImage = new PostImage();
                    postImage.setPost(savedPost);
                    postImage.setImageUrl(imageUrl);
                    postImageRepository.save(postImage);
                    imageUrls.add(imageUrl);
                } catch (Exception e) {
                    throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다.", e);
                }
            }
        }

        return new PostResponseDto(
                savedPost.getId(),
                savedPost.getTitle(),
                savedPost.getContent(),
                savedPost.getCreatedAt().toString(),
                imageUrls,
                tagNames != null ? tagNames : new ArrayList<>()
        );
    }

    // 태그명으로 게시글 검색
    @Transactional(readOnly = true)
    public List<PostResponseDto> getPostsByTagName(String tagName) {
        List<Post> posts = postRepository.findAllByTags_Name(tagName);

        return posts.stream()
                .map(post -> new PostResponseDto(
                        post.getId(),
                        post.getTitle(),
                        post.getContent(),
                        post.getCreatedAt().toString(),
                        post.getImages().stream().map(PostImage::getImageUrl).collect(Collectors.toList()),
                        post.getTags().stream().map(Tag::getName).collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }
}
