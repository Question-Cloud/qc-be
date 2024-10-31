package com.eager.questioncloud.core.domain.authentication.implement;

import com.eager.questioncloud.core.domain.user.implement.PasswordProcessor;
import com.eager.questioncloud.core.domain.user.implement.UserReader;
import com.eager.questioncloud.core.domain.user.model.User;
import com.eager.questioncloud.core.domain.user.vo.AccountType;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationProcessor {
    private final UserReader userReader;

    public User getUserByCredentials(String email, String password) {
        User user = userReader.getUserByEmail(email);
        if (!PasswordProcessor.matches(password, user.getPassword())) {
            throw new CustomException(Error.FAIL_LOGIN);
        }
        user.checkUserStatus();
        return user;
    }

    public Optional<User> getUserBySocialUid(AccountType accountType, String socialUid) {
        return userReader.getSocialUser(accountType, socialUid);
    }
}
