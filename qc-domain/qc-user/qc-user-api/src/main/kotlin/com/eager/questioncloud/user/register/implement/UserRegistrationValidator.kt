package com.eager.questioncloud.user.register.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.user.domain.UserAccountInformation
import com.eager.questioncloud.user.domain.UserInformation
import com.eager.questioncloud.user.infrastructure.repository.UserRepository
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
