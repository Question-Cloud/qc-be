package com.eager.questioncloud.application.api.user.profile;

import com.eager.questioncloud.domain.user.User;
import com.eager.questioncloud.domain.user.UserRepository;
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
