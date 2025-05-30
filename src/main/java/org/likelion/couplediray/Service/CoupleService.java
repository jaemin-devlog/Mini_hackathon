package org.likelion.couplediray.Service;

import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.likelion.couplediray.Entity.Couple;
import org.likelion.couplediray.Entity.CoupleUser;
import org.likelion.couplediray.Entity.User;
import org.likelion.couplediray.Repository.CoupleRepository;
import org.likelion.couplediray.Repository.CoupleUserRepository;
import org.likelion.couplediray.utill.InviteCodeGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Getter
@Setter
@Service
@RequiredArgsConstructor
public class CoupleService {

    private final CoupleRepository coupleRepository;
    private final CoupleUserRepository coupleUserRepository;
    private final InviteCodeGenerator inviteCodeGenerator;

    @Transactional
    public String createInviteCode(User user) {
        // 이미 커플로 연결됐으면 초대코드 못 생성
        if (!coupleUserRepository.findByUser(user).isEmpty()) {
            throw new RuntimeException("이미 커플로 연결된 사용자입니다.");
        }

        String inviteCode;
        do {
            inviteCode = inviteCodeGenerator.generateInviteCode();
        } while (coupleRepository.findByInviteCode(inviteCode).isPresent());

        // 커플 엔티티 생성만 (유저 연결 안함!)
        Couple couple = new Couple();
        couple.setInviteCode(inviteCode);
        couple.setCreator(user);
        coupleRepository.save(couple);

        return inviteCode;
    }

    @Transactional
    public void joinCouple(User user, String inviteCode) {
        // 이미 연결된 유저는 다시 연결 못함
        if (!coupleUserRepository.findByUser(user).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 커플로 연결된 사용자입니다.");
        }

        Couple couple = coupleRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "초대코드가 유효하지 않습니다."));

        List<CoupleUser> existing = coupleUserRepository.findAllByCouple(couple);

        if (existing.size() >= 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 커플이 모두 연결되어 있습니다.");
        }

        // ✅ creator 가져오기
        User creator = couple.getCreator();
        if (creator == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "초대코드 생성자가 존재하지 않습니다.");
        }

        // ✅ creator 아직 등록 안 됐으면 같이 연결
        boolean creatorAlreadyConnected = existing.stream()
                .anyMatch(cu -> cu.getUser().getUserId().equals(creator.getUserId()));

        if (!creatorAlreadyConnected) {
            CoupleUser creatorCU = new CoupleUser();
            creatorCU.setUser(creator);
            creatorCU.setCouple(couple);
            coupleUserRepository.save(creatorCU);
        }

        // ✅ join 요청자 등록
        CoupleUser coupleUser = new CoupleUser();
        coupleUser.setUser(user);
        coupleUser.setCouple(couple);
        coupleUserRepository.save(coupleUser);
    }
}