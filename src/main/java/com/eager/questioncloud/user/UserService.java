package com.eager.questioncloud.user;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserReader userReader;

    public Optional<User> getSocialUser(AccountType accountType, String socialUid) {
        return userReader.getSocialUser(accountType, socialUid);
    }
}
