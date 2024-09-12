package com.eager.questioncloud.user;

import com.eager.questioncloud.user.UserDto.UserWithCreator;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReader {
    private final UserRepository userRepository;

    public Optional<User> getSocialUser(AccountType accountType, String socialUid) {
        return userRepository.getSocialUser(accountType, socialUid);
    }

    public User getUser(Long uid) {
        return userRepository.getUser(uid);
    }

    public UserWithCreator getUserWithCreator(Long uid) {
        return userRepository.getUserWithCreatorId(uid);
    }

    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }
}
