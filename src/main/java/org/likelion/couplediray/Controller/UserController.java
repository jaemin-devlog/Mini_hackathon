package org.likelion.couplediray.Controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.likelion.couplediray.Entity.Couple;
import org.likelion.couplediray.Entity.CoupleUser;
import org.likelion.couplediray.Entity.User;
import org.likelion.couplediray.Repository.CoupleUserRepository;
import org.likelion.couplediray.Repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.io.File;
@RestController
@RequiredArgsConstructor
@Transactional
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;
    private final CoupleUserRepository coupleUserRepository;
    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(HttpSession session) {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof User)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        User user = (User) loginUser;

        return ResponseEntity.ok(Map.of(
                "nickname", user.getNickname(),
                "email", user.getEmail()  // 필요시 더 추가
        ));
    }
    @PostMapping("/status-message")
    public ResponseEntity<?> updateStatusMessage(@RequestBody Map<String, String> request, HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");
        }

        String message = request.get("statusMessage");
        user.setStatusMessage(message);
        userRepository.save(user);

        return ResponseEntity.ok("상태메시지 저장 완료");
    }

    @GetMapping("/status-message")
    public ResponseEntity<?> getMyStatusMessage(HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");
        }

        return ResponseEntity.ok(Map.of("statusMessage", user.getStatusMessage()));
    }
    @GetMapping("/partner/status-message")
    public ResponseEntity<?> getPartnerStatusMessage(HttpSession session) {
        User me = (User) session.getAttribute("loginUser");
        if (me == null) {
            System.out.println("로그인 안 된 상태");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");
        }

        System.out.println("내 아이디: " + me.getUserId());

        CoupleUser myCoupleUser = coupleUserRepository.findByUser(me).stream().findFirst().orElse(null);
        if (myCoupleUser == null) {
            System.out.println("나의 커플User 정보 없음");
            return ResponseEntity.ok(Map.of("statusMessage", ""));
        }

        Couple couple = myCoupleUser.getCouple();
        System.out.println("커플 ID: " + couple.getId());

        List<CoupleUser> usersInCouple = coupleUserRepository.findByCouple(couple);
        System.out.println("이 커플에 몇 명 있음? " + usersInCouple.size());

        for (CoupleUser cu : usersInCouple) {
            System.out.println(" - 유저 ID: " + cu.getUser().getUserId());
        }

        User partner = usersInCouple.stream()
                .map(CoupleUser::getUser)
                .filter(u -> !u.getUserId().equals(me.getUserId()))
                .findFirst()
                .orElse(null);

        if (partner == null) {
            System.out.println("상대방을 찾지 못함");
            return ResponseEntity.ok(Map.of("statusMessage", ""));
        }

        System.out.println("상대방 메시지: " + partner.getStatusMessage());

        return ResponseEntity.ok(Map.of("statusMessage", partner.getStatusMessage()));
    }
    @GetMapping("/partner/info")
    public ResponseEntity<?> getPartnerInfo(HttpSession session) {
        User me = (User) session.getAttribute("loginUser");
        if (me == null) return ResponseEntity.status(401).body("로그인 필요");

        CoupleUser myCoupleUser = coupleUserRepository.findByUser(me).stream().findFirst().orElse(null);
        if (myCoupleUser == null) return ResponseEntity.ok(Map.of());

        Couple couple = myCoupleUser.getCouple();
        List<CoupleUser> users = coupleUserRepository.findByCouple(couple);

        User partner = users.stream()
                .map(CoupleUser::getUser)
                .filter(u -> !u.getUserId().equals(me.getUserId()))
                .findFirst()
                .orElse(null);

        if (partner == null) return ResponseEntity.ok(Map.of());

        return ResponseEntity.ok(Map.of(
                "nickname", partner.getNickname(),
                "statusMessage", Optional.ofNullable(partner.getStatusMessage()).orElse(""),
                "profileImageUrl", Optional.ofNullable(partner.getProfileImageUrl()).orElse("")
        ));
    }
    @PostMapping("/profile-image")
    public ResponseEntity<?> uploadProfileImage(@RequestParam("image") MultipartFile image, HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");
        }

        try {
            // 1. 저장 경로 설정
            String uploadDir = "uploads/profile/";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            // 2. 고유 파일 이름 생성
            String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
            File destination = new File(uploadDir + filename);

            // 3. 저장
            image.transferTo(destination);

            // 4. DB에 경로 저장 (상대경로로)
            String relativePath = uploadDir + filename;
            user.setProfileImageUrl(relativePath);
            userRepository.save(user);

            // 5. 응답
            return ResponseEntity.ok(Map.of("imageUrl", relativePath));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("파일 저장 실패");
        }
    }
}