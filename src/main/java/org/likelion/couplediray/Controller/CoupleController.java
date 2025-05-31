package org.likelion.couplediray.Controller;

import lombok.RequiredArgsConstructor;
import org.likelion.couplediray.DTO.JoinRequest;
import org.likelion.couplediray.Entity.User;
import org.likelion.couplediray.Repository.UserRepository;
import org.likelion.couplediray.Service.CoupleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/couple")
public class CoupleController {

    private final CoupleService coupleService;
    private final UserRepository userRepository;

    // 초대코드 생성
    @PostMapping("/create-code")
    public ResponseEntity<String> createInviteCode(Authentication authentication) {
        User user = (User) authentication.getPrincipal(); // ✅ Spring Security에서 유저 꺼냄
        String inviteCode = coupleService.createInviteCode(user);
        return ResponseEntity.ok(inviteCode);
    }

    // 초대코드로 커플 연결
    @PostMapping("/join")
    public ResponseEntity<String> joinCouple(Authentication authentication,
                                             @RequestBody JoinRequest request) {
        User user = (User) authentication.getPrincipal(); // ✅ 세션에서 꺼내는 거 X
        coupleService.joinCouple(user, request.getInviteCode());
        return ResponseEntity.ok("커플 연결 완료");
    }

}