package com.eager.questioncloud.core.domain.user.service;

import com.eager.questioncloud.core.domain.user.implement.UserUpdater;
import com.eager.questioncloud.core.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserUpdater userUpdater;

    public void updateUserInformation(User user, String name, String profileImage) {
        userUpdater.updateUserInformation(user, name, profileImage);
    }
}
