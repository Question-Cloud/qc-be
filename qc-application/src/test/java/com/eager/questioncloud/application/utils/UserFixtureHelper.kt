package com.eager.questioncloud.application.utils

import com.eager.questioncloud.core.domain.user.dto.CreateUser
import com.eager.questioncloud.core.domain.user.enums.AccountType
import com.eager.questioncloud.core.domain.user.enums.UserStatus
import com.eager.questioncloud.core.domain.user.enums.UserType
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.domain.user.model.User
import com.eager.questioncloud.core.domain.user.model.User.Companion.create
import com.eager.questioncloud.core.domain.user.model.UserAccountInformation.Companion.createEmailAccountInformation
import com.eager.questioncloud.core.domain.user.model.UserAccountInformation.Companion.createSocialAccountInformation
import com.eager.questioncloud.core.domain.user.model.UserInformation.Companion.create
import kotlin.random.Random

class UserFixtureHelper {
    companion object {
        val defaultEmailUserEmail = "test@test.com"
        val defaultEmailUserPassword = "qwer1234"

        val defaultSocialUserEmail = "social@test.com"
        val defaultSocialUserAccountType = AccountType.KAKAO
        val defaultSocialUserSocialUid = "12345678"

        fun createDefaultEmailUser(
            userRepository: UserRepository
        ): User {
            val createUser = CreateUser(
                defaultEmailUserEmail,
                defaultEmailUserPassword,
                null,
                AccountType.EMAIL,
                "01012345678",
                "김승환"
            )
            val userAccountInformation = createEmailAccountInformation(defaultEmailUserPassword)
            val userInformation = create(createUser)
            return userRepository.save(
                create(
                    userAccountInformation = userAccountInformation,
                    userInformation = userInformation,
                    userType = UserType.NormalUser,
                    userStatus = UserStatus.Active,
                )
            )
        }

        fun createEmailUser(
            email: String,
            password: String,
            userStatus: UserStatus,
            userRepository: UserRepository
        ): User {
            val createUser = CreateUser(
                email,
                password,
                null,
                AccountType.EMAIL,
                "010${randomPhoneNumber()}",
                "김승환"
            )
            val userAccountInformation = createEmailAccountInformation(defaultEmailUserPassword)
            val userInformation = create(createUser)
            return userRepository.save(
                create(
                    userAccountInformation,
                    userInformation,
                    UserType.NormalUser,
                    userStatus
                )
            )
        }

        fun createDefaultSocialUser(userRepository: UserRepository): User {
            val createUser =
                CreateUser(
                    defaultSocialUserEmail,
                    null,
                    null,
                    defaultSocialUserAccountType,
                    "010${randomPhoneNumber()}",
                    "김승환"
                )

            val userAccountInformation =
                createSocialAccountInformation(defaultSocialUserSocialUid, defaultSocialUserAccountType)
            val userInformation = create(createUser)
            return userRepository.save(
                create(
                    userAccountInformation = userAccountInformation,
                    userInformation = userInformation,
                    userType = UserType.NormalUser,
                    userStatus = UserStatus.Active
                )
            )
        }

        private fun randomPhoneNumber(): String {
            val randomNumber = Random.nextInt(100_000_000)
            return randomNumber.toString().padStart(8, '0')
        }
    }
}