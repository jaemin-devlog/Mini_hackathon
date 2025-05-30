package org.likelion.couplediray.Controller;

import lombok.RequiredArgsConstructor;
import org.likelion.couplediray.Entity.User;
import org.likelion.couplediray.Repository.CoupleUserRepository;
import org.likelion.couplediray.Service.DiaryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diary")
public class DiaryController {

    private final DiaryService diaryService;
    private final CoupleUserRepository coupleUserRepository;

    private User getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        return (User) authentication.getPrincipal();
    }

    private void validateCoupleConnection(User user) {
        if (coupleUserRepository.findByUser(user).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "커플로 연결된 사용자만 이용 가능");
        }
    }

    @GetMapping("/init")
    public ResponseEntity<Map<String, String>> initDiary(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        boolean isConnected = !coupleUserRepository.findByUser(user).isEmpty();

        if (!isConnected) {
            return ResponseEntity.status(403).body(Map.of("message", "초대코드를 입력해주세요"));
        }

        return ResponseEntity.ok(Map.of("message", "커플 연결 완료"));
    }

    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> saveDiary(
            Authentication authentication,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("content") String content) {

        User user = getAuthenticatedUser(authentication);
        validateCoupleConnection(user);

        diaryService.saveDiary(user, date, content);
        return ResponseEntity.ok(Map.of("message", "일기 저장 완료"));
    }

    @GetMapping("/read")
    public ResponseEntity<Map<String, String>> readDiary(
            Authentication authentication,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        User user = getAuthenticatedUser(authentication);
        validateCoupleConnection(user);

        String content = diaryService.getDiary(user, date);
        return ResponseEntity.ok(Map.of("content", content));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteDiary(
            Authentication authentication,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        User user = getAuthenticatedUser(authentication);
        validateCoupleConnection(user);

        diaryService.deleteDiary(user, date);
        return ResponseEntity.ok(Map.of("message", "일기 삭제 완료"));
    }
}
