package com.eager.questioncloud.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordProcessor {
    public static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public static Boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
