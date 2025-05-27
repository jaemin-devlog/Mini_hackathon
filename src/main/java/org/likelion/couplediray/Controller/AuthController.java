package org.likelion.couplediray.Controller;

import lombok.RequiredArgsConstructor;
import org.likelion.couplediray.DTO.LoginRequest;
import org.likelion.couplediray.DTO.SignupRequest;
import org.likelion.couplediray.Entity.User;
import org.likelion.couplediray.Service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService signupService;
    private final AuthService authService;
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        try {
            User savedUser = signupService.signup(request);
            return ResponseEntity.ok("회원가입 성공!! id=" + savedUser.getUserId());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            User user = authService.login(request);
            return ResponseEntity.ok("환영합니다. " + user.getNickname()+" 님");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

}
