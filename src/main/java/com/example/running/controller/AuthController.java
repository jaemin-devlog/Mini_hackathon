package com.example.running.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.example.running.CommonResponse;
import com.example.running.dto.LoginRequestDto;
import com.example.running.dto.LoginResponseDto;
import com.example.running.entity.User;
import com.example.running.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<CommonResponse<LoginResponseDto>> login(@RequestBody LoginRequestDto request,
                                                                  HttpServletRequest httpRequest) {
        User user = authService.login(request);

        // ✅ 세션 생성 및 사용자 정보 저장
        HttpSession session = httpRequest.getSession(true); // true: 없으면 새로 만듦
        session.setAttribute("loginUser", user); // key 이름은 필요시 상수로 관리

        LoginResponseDto response = new LoginResponseDto(user);
        return ResponseEntity.ok()
                .body(CommonResponse.<LoginResponseDto>builder()
                        .statusCode(200)
                        .msg("로그인 성공")
                        .data(response)
                        .build());
    }

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<LoginResponseDto>> signup(@RequestBody LoginRequestDto request) {
        User user = authService.signup(request);
        LoginResponseDto response = new LoginResponseDto(user);
        return ResponseEntity.ok()
                .body(CommonResponse.<LoginResponseDto>builder()
                        .statusCode(200)
                        .msg("회원가입 성공")
                        .data(response)
                        .build());
    }
}
