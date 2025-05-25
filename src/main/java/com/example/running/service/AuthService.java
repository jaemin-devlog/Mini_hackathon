package com.example.running.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.example.running.dto.LoginRequestDto;
import com.example.running.entity.User;
import com.example.running.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    public User login(LoginRequestDto request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("아이디가 틀렸습니다."));
        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }
        return user;
    }

    public User signup(LoginRequestDto request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }
        User user = request.toEntity();
        return userRepository.save(user);
    }
}
