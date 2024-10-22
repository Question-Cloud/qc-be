package com.eager.questioncloud.domain.authentication.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordProcessor {
    public static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String encode(String rawPassword) {
        if (rawPassword == null) {
            return null;
        }
        return passwordEncoder.encode(rawPassword);
    }

    public static Boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
