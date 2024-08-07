package com.eager.questioncloud.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserService {
    private final UserCreator userCreator;

    public User create(User user) {
        return userCreator.create(user);
    }
}
