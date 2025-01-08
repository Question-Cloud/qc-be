package com.eager.questioncloud.application.api.user.register.implement;

import com.eager.questioncloud.core.domain.user.enums.AccountType;
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository;
import com.eager.questioncloud.core.domain.user.model.UserAccountInformation;
import com.eager.questioncloud.core.domain.user.model.UserInformation;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRegistrationValidator {
    private final UserRepository userRepository;

    public void validate(UserAccountInformation userAccountInformation, UserInformation userInformation) {
        if (userRepository.checkDuplicateEmail(userInformation.getEmail())) {
            throw new CoreException(Error.DUPLICATE_EMAIL);
        }

        if (userRepository.checkDuplicatePhone(userInformation.getPhone())) {
            throw new CoreException(Error.DUPLICATE_PHONE);
        }

        if (isSocialAccount(userAccountInformation)
            &&
            userRepository.checkDuplicateSocialUidAndAccountType(userAccountInformation.getSocialUid(), userAccountInformation.getAccountType())) {
            throw new CoreException(Error.DUPLICATE_SOCIAL_UID);
        }
    }

    private Boolean isSocialAccount(UserAccountInformation userAccountInformation) {
        return !userAccountInformation.getAccountType().equals(AccountType.EMAIL);
    }
}
