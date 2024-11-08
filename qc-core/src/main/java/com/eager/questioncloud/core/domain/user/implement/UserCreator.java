package com.eager.questioncloud.core.domain.user.implement;

import com.eager.questioncloud.core.domain.user.model.User;
import com.eager.questioncloud.core.domain.user.repository.UserRepository;
import com.eager.questioncloud.core.domain.user.vo.AccountType;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCreator {
    private final UserRepository userRepository;

    public User create(User user) {
        checkDuplicateEmail(user.getUserInformation().getEmail());
        checkDuplicatePhone(user.getUserInformation().getPhone());
        checkDuplicateSocialUidAndAccountType(user.getUserAccountInformation().getSocialUid(), user.getUserAccountInformation().getAccountType());
        return userRepository.save(user);
    }

    private void checkDuplicatePhone(String phone) {
        if (userRepository.checkDuplicatePhone(phone)) {
            throw new CustomException(Error.DUPLICATE_EMAIL);
        }
    }

    private void checkDuplicateEmail(String email) {
        if (userRepository.checkDuplicateEmail(email)) {
            throw new CustomException(Error.DUPLICATE_PHONE);
        }
    }

    private void checkDuplicateSocialUidAndAccountType(String socialUid, AccountType accountType) {
        if (userRepository.checkDuplicateSocialUidAndAccountType(socialUid, accountType)) {
            throw new CustomException(Error.DUPLICATE_SOCIAL_UID);
        }
    }
}
