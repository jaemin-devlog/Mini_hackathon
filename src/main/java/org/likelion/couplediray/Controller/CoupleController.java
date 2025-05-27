package org.likelion.couplediray.Controller;

import lombok.RequiredArgsConstructor;
import org.likelion.couplediray.DTO.CreateCoupleRequest;
import org.likelion.couplediray.DTO.JoinRequest;
import org.likelion.couplediray.Entity.User;
import org.likelion.couplediray.Repository.UserRepository;
import org.likelion.couplediray.Service.CoupleService;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody CreateCoupleRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("해당 유저 없음"));

        String code = coupleService.createCouple(user);
        return ResponseEntity.ok("초대 코드: " + code);
    }

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody JoinRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("해당 유저 없음"));

        coupleService.joinCouple(user, request.getCode());
        return ResponseEntity.ok("커플 탄생");
    }
}