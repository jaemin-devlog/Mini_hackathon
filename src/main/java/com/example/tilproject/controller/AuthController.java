package com.example.tilproject.controller;

import com.example.tilproject.CommonResponse;
import com.example.tilproject.dto.LoginRequestDto;
import com.example.tilproject.dto.LoginResponseDto;
import com.example.tilproject.entity.User;
import com.example.tilproject.security.JwtTokenProvider;
import com.example.tilproject.security.UserPrincipal;
import com.example.tilproject.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Auth", description = "로그인 관련 API")
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<CommonResponse<LoginResponseDto>> login(@RequestBody LoginRequestDto request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 로그인 응답 DTO 반환
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();
        String token = jwtTokenProvider.createToken(user.getUsername(), user.getUserId());
        LoginResponseDto response = LoginResponseDto.of(
                user.getUserId(),
                user.getUsername(),
                token,
                user.getRole()
        );

        return ResponseEntity.ok(
                CommonResponse.<LoginResponseDto>builder()
                        .statusCode(200)
                        .msg("로그인 성공")
                        .data(response)
                        .build()
        );
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<LoginResponseDto>> signup(@RequestBody LoginRequestDto request) {

        User user = authService.signup(request);
        LoginResponseDto response = LoginResponseDto.of(
                user.getUserId(),
                user.getUsername(),
                null, //회원가입떄는 토큰 발행X
                user.getRole()
        );
        return ResponseEntity.ok(
                CommonResponse.<LoginResponseDto>builder()
                        .statusCode(200)
                        .msg("회원가입 성공")
                        .data(response)
                        .build()
        );
    }

}
