package org.likelion.couplediray.utill;

import java.util.UUID;

public class InviteCodeGenerator {
    public static String generateCode() {
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}