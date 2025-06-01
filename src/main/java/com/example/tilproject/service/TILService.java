package com.example.tilproject.service;

import com.example.tilproject.dto.TILRequestDto;
import com.example.tilproject.entity.TIL;
import com.example.tilproject.entity.User;
import com.example.tilproject.repository.FriendRepository;
import com.example.tilproject.repository.TILRepository;
import com.example.tilproject.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TILService {
    private final TILRepository tilRepository;
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    public TILService(TILRepository tilRepository, UserRepository userRepository, FriendRepository friendRepository) {
        this.tilRepository = tilRepository;
        this.userRepository = userRepository;
        this.friendRepository = friendRepository;
    }


    public TIL createTIL(TILRequestDto request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        TIL til = request.toEntity(user);

        return tilRepository.save(til);
    }

    public TIL getTIL(Long tilId, Long userId) {
        TIL til = tilRepository.findById(tilId)
                .orElseThrow(() -> new IllegalArgumentException("TIL이 존재하지 않습니다."));

        Long ownerId = til.getUser().getUserId();

        if (!ownerId.equals(userId)) {
            if (!friendRepository.isFriend(ownerId, userId)) {
                throw new AccessDeniedException("친구가 아닌 사용자의 TIL에는 접근할 수 없습니다.");
            }
        }

        return til;
    }


    public TIL updateTIL(Long tilId, TILRequestDto request, Long userId) {
        TIL til = getTILOrThrow(tilId);
        if(!til.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("수정할 권한이 없습니다.");
        }

        til.setTitle(request.getTitle());
        til.setContent(request.getContent());
        return tilRepository.save(til);
    }

    public void deleteTIL(Long tilId, Long userId) {
        TIL til = getTILOrThrow(tilId);
        if(!til.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        tilRepository.delete(til);
    }

    private TIL getTILOrThrow(Long tilId) {
        return tilRepository.findById(tilId)
                .orElseThrow(()-> new RuntimeException("해당 TIL을 찾을 수 없습니다."));
    }

    public List<TIL> getTILs(Long userId) {
        return tilRepository.findAllByUser_UserId(userId);
    }

}