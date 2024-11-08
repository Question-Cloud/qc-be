package com.eager.questioncloud.core.domain.user.implement;

import com.eager.questioncloud.core.domain.user.model.User;
import com.eager.questioncloud.core.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUpdater {
    private final UserRepository userRepository;

    public void verifyUser(User user) {
        user.active();
        userRepository.save(user);
    }

    public void updateUser(User user, String name, String profileImage) {
        userRepository.save(user.update(name, profileImage));
    }

    public void changePassword(User user, String newPassword) {
        user.changePassword(newPassword);
        userRepository.save(user);
    }

    public void setCreator(User user) {
        user.setCreator();
        userRepository.save(user);
    }
}
