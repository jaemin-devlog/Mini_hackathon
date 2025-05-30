package org.likelion.couplediray.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.likelion.couplediray.DTO.SignupRequest;
import org.likelion.couplediray.Entity.User;
import org.likelion.couplediray.Service.AuthService;
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
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
        try {
            User savedUser = signupService.signup(request);
            return ResponseEntity.ok("회원가입 성공!! id=" + savedUser.getUserId());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
