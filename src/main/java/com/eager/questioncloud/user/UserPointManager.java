package com.eager.questioncloud.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPointManager {
    private final UserReader userReader;
    private final UserUpdater userUpdater;

    public void usePoint(Long uid, int amount) {
        User user = userReader.getUser(uid);
        user.usePoint(amount);
        userUpdater.updateUser(user);
    }
}
