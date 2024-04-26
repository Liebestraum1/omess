package org.sixback.omess.common.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtils {
    private final static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public static boolean isNotValidPassword(String inputPassword, String encodedPassword) {
        return !passwordEncoder.matches(inputPassword, encodedPassword);
    }
}
