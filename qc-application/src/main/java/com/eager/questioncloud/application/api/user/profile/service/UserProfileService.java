package com.eager.questioncloud.application.api.user.profile.service;

import com.eager.questioncloud.core.domain.user.infrastructure.UserRepository;
import com.eager.questioncloud.core.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserRepository userRepository;

    public void updateUserInformation(User user, String name, String profileImage) {
        user.updateUserInformation(name, profileImage);
        userRepository.save(user);
    }
}
