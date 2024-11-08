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
public class UserRegister {
    private final UserRepository userRepository;

    public User register(User user) {
        if (checkDuplicateEmail(user.getUserInformation().getEmail())) {
            throw new CustomException(Error.DUPLICATE_EMAIL);
        }
        if (checkDuplicatePhone(user.getUserInformation().getPhone())) {
            throw new CustomException(Error.DUPLICATE_PHONE);
        }
        if (checkDuplicateSocialUidAndAccountType(
            user.getUserAccountInformation().getSocialUid(),
            user.getUserAccountInformation().getAccountType())) {
            throw new CustomException(Error.DUPLICATE_SOCIAL_UID);
        }
        return userRepository.save(user);
    }

    public Boolean checkDuplicatePhone(String phone) {
        return userRepository.checkDuplicatePhone(phone);
    }

    public Boolean checkDuplicateEmail(String email) {
        return userRepository.checkDuplicateEmail(email);
    }

    public Boolean checkDuplicateSocialUidAndAccountType(String socialUid, AccountType accountType) {
        return userRepository.checkDuplicateSocialUidAndAccountType(socialUid, accountType);
    }
}
