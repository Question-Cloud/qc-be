package com.eager.questioncloud.application.business.user.implement;

import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository;
import com.eager.questioncloud.core.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAccountUpdater {
    private final UserRepository userRepository;

    public void changePassword(Long userId, String newPassword) {
        User user = userRepository.getUser(userId);
        user.changePassword(newPassword);
        userRepository.save(user);
    }
}
