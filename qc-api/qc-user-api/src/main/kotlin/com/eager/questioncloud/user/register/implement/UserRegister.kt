package com.eager.questioncloud.user.register.implement

import com.eager.questioncloud.point.implement.UserPointManager
import com.eager.questioncloud.social.SocialAPIManager
import com.eager.questioncloud.social.SocialPlatform
import com.eager.questioncloud.user.domain.User
import com.eager.questioncloud.user.domain.UserAccountInformation
import com.eager.questioncloud.user.domain.UserInformation
import com.eager.questioncloud.user.dto.CreateUser
import com.eager.questioncloud.user.enums.AccountType
import com.eager.questioncloud.user.enums.UserStatus
import com.eager.questioncloud.user.enums.UserType
import com.eager.questioncloud.user.infrastructure.repository.UserRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserRegister(
    private val userRegistrationValidator: UserRegistrationValidator,
    private val userPointManager: UserPointManager, // TODO 임시 의존성
    private val socialAPIManager: SocialAPIManager,
    private val userRepository: UserRepository,
) {
    @Transactional
    fun create(createUser: CreateUser): User {
        val userAccountInformation = createUserAccountInformation(createUser)
        val userInformation = UserInformation.create(createUser)
        userRegistrationValidator.validate(userAccountInformation, userInformation)

        val user = userRepository.save(
            User.create(
                userAccountInformation,
                userInformation,
                UserType.NormalUser,
                UserStatus.PendingEmailVerification
            )
        )

        userPointManager.init(user.uid)
        return user
    }

    private fun createUserAccountInformation(createUser: CreateUser): UserAccountInformation {
        if (createUser.accountType == AccountType.EMAIL) {
            return UserAccountInformation.createEmailAccountInformation(createUser.password!!)
        }

        val socialUid = socialAPIManager.getSocialUid(
            createUser.socialRegisterToken!!,
            SocialPlatform.valueOf(createUser.accountType.value)
        )

        return UserAccountInformation.createSocialAccountInformation(socialUid, createUser.accountType)
    }
}
