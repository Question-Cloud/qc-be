package com.eager.questioncloud.core.domain.user.model

import com.eager.questioncloud.core.domain.user.enums.UserStatus
import com.eager.questioncloud.core.domain.user.enums.UserType
import com.eager.questioncloud.core.domain.user.implement.PasswordProcessor
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import com.eager.questioncloud.core.exception.NotVerificationUserException

class User(
    var uid: Long? = null,
    var userAccountInformation: UserAccountInformation,
    var userInformation: UserInformation,
    var userType: UserType? = null,
    var userStatus: UserStatus? = null,
) {
    fun active() {
        this.userStatus = UserStatus.Active
    }

    fun checkUserStatus() {
        if (userStatus == UserStatus.PendingEmailVerification) {
            throw NotVerificationUserException(uid)
        }
        if (userStatus != UserStatus.Active) {
            throw CoreException(Error.NOT_ACTIVE_USER)
        }
    }

    fun changePassword(newPassword: String) {
        userAccountInformation = userAccountInformation.changePassword(newPassword)
    }

    fun passwordAuthentication(rawPassword: String) {
        if (!PasswordProcessor.matches(rawPassword, userAccountInformation.password!!)) {
            throw CoreException(Error.FAIL_LOGIN)
        }
    }

    fun updateUserInformation(name: String, profileImage: String) {
        userInformation = userInformation.updateUserInformation(name, profileImage)
    }

    fun setCreator() {
        this.userType = UserType.CreatorUser
    }

    companion object {
        @JvmStatic
        fun create(
            userAccountInformation: UserAccountInformation,
            userInformation: UserInformation,
            userType: UserType,
            userStatus: UserStatus
        ): User {
            return User(
                userAccountInformation = userAccountInformation,
                userInformation = userInformation,
                userType = userType,
                userStatus = userStatus
            )
        }

        @JvmStatic
        fun guest(): User {
            return User(
                uid = -1L,
                userAccountInformation = UserAccountInformation.guestAccountInformation,
                userInformation = UserInformation.guestInformation,
            )
        }
    }
}
