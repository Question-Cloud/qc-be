package com.eager.questioncloud.user.implement;

import com.eager.questioncloud.user.model.User;
import com.eager.questioncloud.user.repository.UserRepository;
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

    public User updateUser(User user, String name, String profileImage) {
        return userRepository.save(user.update(name, profileImage));
    }

    public User changePassword(User user, String newPassword) {
        user.changePassword(newPassword);
        return userRepository.save(user);
    }
}
