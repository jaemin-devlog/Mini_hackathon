package com.example.running.controller;

import com.example.running.dto.SignupRequestDto;
import com.example.running.dto.SignupResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import com.example.running.CommonResponse;
import com.example.running.dto.LoginRequestDto;
import com.example.running.dto.LoginResponseDto;
import com.example.running.entity.User;
import com.example.running.service.AuthService;
import com.example.running.service.BadgeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final BadgeService badgeService;  // BadgeService 주입

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<LoginResponseDto>> login(@RequestBody LoginRequestDto request,
                                                                  HttpServletRequest httpRequest) {
        User user = authService.login(request);

        HttpSession session = httpRequest.getSession(true);
        session.setAttribute("loginUser", user);

        LoginResponseDto response = new LoginResponseDto(user);
        return ResponseEntity.ok(
                CommonResponse.<LoginResponseDto>builder()
                        .statusCode(200)
                        .msg("로그인 성공")
                        .data(response)
                        .build()
        );
    }

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<SignupResponseDto>> signup(@RequestBody SignupRequestDto request) {
        User user = authService.signup(request);

        // 회원가입 직후 좋아요 0으로 뱃지 부여
        badgeService.checkAndAssignBadge(user, 0);

        SignupResponseDto response = new SignupResponseDto(user.getUserId(), user.getUsername(), user.getNickname());
        return ResponseEntity.ok(
                CommonResponse.<SignupResponseDto>builder()
                        .statusCode(200)
                        .msg("회원가입 성공")
                        .data(response)
                        .build()
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<CommonResponse<String>> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 세션이 없으면 null
        if (session != null) {
            session.invalidate(); // 세션 무효화
        }

        return ResponseEntity.ok(
                CommonResponse.<String>builder()
                        .statusCode(200)
                        .msg("로그아웃 성공")
                        .data("세션이 종료되었습니다.")
                        .build()
        );
    }
}
