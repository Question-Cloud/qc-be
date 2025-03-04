package com.eager.questioncloud.application.business.user.implement

import com.eager.questioncloud.core.domain.point.implement.UserPointManager
import com.eager.questioncloud.core.domain.user.dto.CreateUser
import com.eager.questioncloud.core.domain.user.enums.AccountType
import com.eager.questioncloud.core.domain.user.enums.UserStatus
import com.eager.questioncloud.core.domain.user.enums.UserType
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.domain.user.model.User
import com.eager.questioncloud.core.domain.user.model.User.Companion.create
import com.eager.questioncloud.core.domain.user.model.UserAccountInformation
import com.eager.questioncloud.core.domain.user.model.UserAccountInformation.Companion.createEmailAccountInformation
import com.eager.questioncloud.core.domain.user.model.UserAccountInformation.Companion.createSocialAccountInformation
import com.eager.questioncloud.core.domain.user.model.UserInformation
import com.eager.questioncloud.social.SocialAPIManager
import com.eager.questioncloud.social.SocialPlatform
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserRegister(
    private val userRegistrationValidator: UserRegistrationValidator,
    private val userPointManager: UserPointManager,
    private val socialAPIManager: SocialAPIManager,
    private val userRepository: UserRepository,
) {
    @Transactional
    fun create(createUser: CreateUser): User {
        val userAccountInformation = createUserAccountInformation(createUser)
        val userInformation = UserInformation.create(createUser)
        userRegistrationValidator.validate(userAccountInformation, userInformation)

        val user = userRepository.save(
            create(userAccountInformation, userInformation, UserType.NormalUser, UserStatus.PendingEmailVerification)
        )

        userPointManager.init(user.uid!!)
        return user
    }

    private fun createUserAccountInformation(createUser: CreateUser): UserAccountInformation {
        if (createUser.accountType == AccountType.EMAIL) {
            return createEmailAccountInformation(createUser.password!!)
        }
        
        val socialUid = socialAPIManager.getSocialUid(
            createUser.socialRegisterToken!!,
            SocialPlatform.valueOf(createUser.accountType.value)
        )
        return createSocialAccountInformation(socialUid, createUser.accountType)
    }
}
