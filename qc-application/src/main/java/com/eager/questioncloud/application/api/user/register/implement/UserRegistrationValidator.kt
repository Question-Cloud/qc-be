package com.eager.questioncloud.application.api.user.register.implement

import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.domain.user.model.UserAccountInformation
import com.eager.questioncloud.core.domain.user.model.UserInformation
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import org.springframework.stereotype.Component

@Component
class UserRegistrationValidator(
    private val userRepository: UserRepository
) {
    fun validate(userAccountInformation: UserAccountInformation, userInformation: UserInformation) {
        if (userRepository.checkDuplicateEmail(userInformation.email)) {
            throw CoreException(Error.DUPLICATE_EMAIL)
        }

        if (userRepository.checkDuplicatePhone(userInformation.phone)) {
            throw CoreException(Error.DUPLICATE_PHONE)
        }

        if (userAccountInformation.isSocialAccount
            &&
            userRepository.checkDuplicateSocialUidAndAccountType(
                userAccountInformation.socialUid!!,
                userAccountInformation.accountType
            )
        ) {
            throw CoreException(Error.DUPLICATE_SOCIAL_UID)
        }
    }
}
