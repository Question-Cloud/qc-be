package com.eager.questioncloud.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUpdater {
    private final UserRepository userRepository;

    public User verifyUser(User user) {
        user.active();
        return userRepository.save(user);
    }
}
