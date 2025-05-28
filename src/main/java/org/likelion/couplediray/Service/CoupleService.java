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
import org.springframework.stereotype.Service;
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
                // 이미 커플 연결 여부 확인
                if (!coupleUserRepository.findByUser(user).isEmpty()) {
                    throw new RuntimeException("이미 커플로 연결된 사용자입니다.");
                }
                // 유일한 초대코드 생성
                String inviteCode;
                do {
                    inviteCode = inviteCodeGenerator.generateInviteCode();
                } while (coupleRepository.findByInviteCode(inviteCode).isPresent());

        Couple couple = new Couple();
        couple.setInviteCode(inviteCode);
        coupleRepository.save(couple);

        CoupleUser coupleUser = new CoupleUser();
        coupleUser.setUser(user);
        coupleUser.setCouple(couple);
        coupleUserRepository.save(coupleUser);

        return inviteCode;
    }

    @Transactional
    public void joinCouple(User user, String inviteCode) {
        // 이미 커플 연결 여부 확인
        if (!coupleUserRepository.findByUser(user).isEmpty()) {
            throw new RuntimeException("이미 커플로 연결된 사용자입니다.");
        }
        // 초대 코드 유효성 확인
        Couple couple = coupleRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 초대코드입니다."));

        CoupleUser coupleUser = new CoupleUser();
        coupleUser.setUser(user);
        coupleUser.setCouple(couple);
        coupleUserRepository.save(coupleUser);
    }
}

