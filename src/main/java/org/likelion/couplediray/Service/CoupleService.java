package org.likelion.couplediray.Service;

import lombok.RequiredArgsConstructor;
import org.likelion.couplediray.Entity.Couple;
import org.likelion.couplediray.Entity.CoupleUser;
import org.likelion.couplediray.Entity.User;
import org.likelion.couplediray.Repository.CoupleRepository;
import org.likelion.couplediray.Repository.CoupleUserRepository;
import org.likelion.couplediray.utill.InviteCodeGenerator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoupleService {

    private final CoupleRepository coupleRepository;
    private final CoupleUserRepository coupleUserRepository;

    public String createCouple(User user) {
        String code = InviteCodeGenerator.generateCode();

        Couple couple = new Couple();
        couple.setInviteCode(code);
        coupleRepository.save(couple);

        CoupleUser coupleUser = new CoupleUser();
        coupleUser.setUser(user);
        coupleUser.setCouple(couple);
        coupleUserRepository.save(coupleUser);

        return code;
    }

    public void joinCouple(User user, String code) {
        Couple couple = coupleRepository.findByInviteCode(code)
                .orElseThrow(() -> new RuntimeException("초대 코드가 유효하지 않습니다."));

        CoupleUser coupleUser = new CoupleUser();
        coupleUser.setUser(user);
        coupleUser.setCouple(couple);
        coupleUserRepository.save(coupleUser);
    }
}