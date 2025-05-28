package org.likelion.couplediray.utill;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class InviteCodeGenerator {

    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int DEFAULT_LENGTH = 6;

    /**
     * 기본 길이({@value #DEFAULT_LENGTH})의 초대코드를 생성합니다.
     * @return 생성된 초대코드
     */
    public String generateInviteCode() {
        return generateInviteCode(DEFAULT_LENGTH);
    }

    /**
     * 지정된 길이의 초대코드를 생성합니다.
     * @param length 코드 길이
     * @return 생성된 초대코드
     */
    public String generateInviteCode(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int idx = RANDOM.nextInt(CHAR_POOL.length());
            sb.append(CHAR_POOL.charAt(idx));
        }
        return sb.toString();
    }
}
