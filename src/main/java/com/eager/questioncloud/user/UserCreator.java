package com.eager.questioncloud.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class UserCreator {
    private final UserRepository userRepository;

    public User create(User user) {
        if (checkDuplicateLoginId(user.getLoginId())) {
            throw new RuntimeException();
        }
        if (checkDuplicateSocialUid(user.getAccountType(), user.getSocialUid())) {
            throw new RuntimeException();
        }
        if (checkDuplicatePhone(user)) {
            throw new RuntimeException();
        }
        return userRepository.append(user);
    }

    public Boolean checkDuplicateLoginId(String loginId) {
        if (StringUtils.hasText(loginId)) {
            return false;
        }
        return userRepository.checkDuplicateLoginId(loginId);
    }

    public Boolean checkDuplicateSocialUid(AccountType accountType, String socialUid) {
        if (accountType.equals(AccountType.ID)) {
            return false;
        }
        return userRepository.checkDuplicateSocialUid(accountType, socialUid);
    }

    public Boolean checkDuplicatePhone(User user) {
        return userRepository.checkDuplicatePhone(user.getPhone());
    }
}
