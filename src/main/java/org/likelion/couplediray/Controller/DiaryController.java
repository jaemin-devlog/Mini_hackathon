package org.likelion.couplediray.Controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.likelion.couplediray.Entity.Couple;
import org.likelion.couplediray.Entity.CoupleUser;
import org.likelion.couplediray.Entity.User;
import org.likelion.couplediray.Repository.CoupleUserRepository;
import org.likelion.couplediray.Service.DiaryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diary")
public class DiaryController {

    private final DiaryService diaryService;
    private final CoupleUserRepository coupleUserRepository;

    private User getSessionUser(HttpSession session) {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof User)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        return (User) loginUser;
    }

    private void validateCoupleConnection(User user) {
        if (coupleUserRepository.findByUserId(user.getUserId()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "커플로 연결된 사용자만 이용 가능");
        }
    }

    @GetMapping("/init")
    public ResponseEntity<Map<String, String>> initDiary(HttpSession session) {
        User user = getSessionUser(session);
        boolean isConnected = !coupleUserRepository.findByUserId(user.getUserId()).isEmpty();

        if (!isConnected) {
            return ResponseEntity.status(403).body(Map.of("message", "초대코드를 입력해주세요"));
        }

        return ResponseEntity.ok(Map.of("message", "커플 연결 완료"));
    }

    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> saveDiary(
            HttpSession session,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("content") String content) {

        User user = getSessionUser(session);
        validateCoupleConnection(user);

        diaryService.saveDiary(user, date, content);
        return ResponseEntity.ok(Map.of("message", "일기 저장 완료"));
    }

    @GetMapping("/read")
    public ResponseEntity<Map<String, String>> readDiary(
            HttpSession session,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        User user = getSessionUser(session);
        validateCoupleConnection(user);

        String content = diaryService.getDiary(user, date);
        return ResponseEntity.ok(Map.of("content", content));
    }


    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteDiary(
            HttpSession session,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        User user = getSessionUser(session);
        validateCoupleConnection(user);

        diaryService.deleteDiary(user, date);
        return ResponseEntity.ok(Map.of("message", "일기 삭제 완료"));
    }

    @GetMapping("/nicknames")
    public ResponseEntity<?> getCoupleNicknames(HttpSession session) {
        User user = getSessionUser(session);

        List<CoupleUser> coupleUsers = coupleUserRepository.findByUserId(user.getUserId());
        if (coupleUsers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("커플이 아님");
        }

        Couple couple = coupleUsers.get(0).getCouple();
        List<CoupleUser> allMembers = coupleUserRepository.findByCouple(couple);

        List<String> nicknames = allMembers.stream()
                .map(cu -> cu.getUser().getNickname())
                .collect(Collectors.toList());

        return ResponseEntity.ok(nicknames);
    }
    @PostMapping("/memo")
    public ResponseEntity<Map<String, String>> saveMemo(
            HttpSession session,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("memo") String memo) {

        User user = getSessionUser(session);
        validateCoupleConnection(user);

        diaryService.saveMemo(user, date, memo);
        return ResponseEntity.ok(Map.of("message", "메모 저장 완료"));
    }

    @GetMapping("/memo")
    public ResponseEntity<Map<String, String>> getMemo(
            HttpSession session,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        User user = getSessionUser(session);
        validateCoupleConnection(user);

        String memo = diaryService.getMemo(user, date);
        return ResponseEntity.ok(Map.of("memo", memo));
    }
    @PostMapping("/bucket")
    public ResponseEntity<Map<String, String>> saveBucketList(
            HttpSession session,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("bucket") String bucket) {

        User user = getSessionUser(session);
        validateCoupleConnection(user);

        diaryService.saveBucketList(user, date, bucket);
        return ResponseEntity.ok(Map.of("message", "버킷리스트 저장 완료"));
    }

    // ✅ 버킷리스트 조회
    @GetMapping("/bucket")
    public ResponseEntity<Map<String, String>> getBucketList(
            HttpSession session,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        User user = getSessionUser(session);
        validateCoupleConnection(user);

        String bucket = diaryService.getBucketList(user, date);
        return ResponseEntity.ok(Map.of("bucket", bucket));
    }
}