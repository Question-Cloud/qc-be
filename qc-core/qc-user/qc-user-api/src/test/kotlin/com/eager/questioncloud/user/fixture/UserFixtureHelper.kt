package com.eager.questioncloud.application.utils.fixture.helper

import com.eager.questioncloud.user.domain.User
import com.eager.questioncloud.user.domain.UserAccountInformation
import com.eager.questioncloud.user.domain.UserInformation
import com.eager.questioncloud.user.dto.CreateUser
import com.eager.questioncloud.user.enums.AccountType
import com.eager.questioncloud.user.enums.UserStatus
import com.eager.questioncloud.user.enums.UserType
import com.eager.questioncloud.user.repository.UserRepository
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
            val userAccountInformation = UserAccountInformation.createEmailAccountInformation(defaultEmailUserPassword)
            val userInformation = UserInformation.create(createUser)
            return userRepository.save(
                User.create(
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
            val userAccountInformation = UserAccountInformation.createEmailAccountInformation(defaultEmailUserPassword)
            val userInformation = UserInformation.create(createUser)
            return userRepository.save(
                User.create(
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
                UserAccountInformation.createSocialAccountInformation(
                    defaultSocialUserSocialUid,
                    defaultSocialUserAccountType
                )
            val userInformation = UserInformation.create(createUser)
            return userRepository.save(
                User.create(
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