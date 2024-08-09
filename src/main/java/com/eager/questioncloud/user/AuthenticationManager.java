package com.eager.questioncloud.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationManager {
    private final UserRepository userRepository;

    public User authentication(String loginId, String password) {
        User user = userRepository.getUserByLoginId(loginId);
        if (!PasswordProcessor.matches(password, user.getPassword())) {
            throw new RuntimeException();
        }
        return user;
    }
}
