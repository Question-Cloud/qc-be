package com.eager.questioncloud.user.domain

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.user.enums.UserStatus
import com.eager.questioncloud.user.enums.UserType

class User(
    var uid: Long = 0,
    var userAccountInformation: UserAccountInformation,
    var userInformation: UserInformation,
    var userType: UserType,
    var userStatus: UserStatus,
) {
    fun active() {
        this.userStatus = UserStatus.Active
    }

    fun checkUserStatus() {
        if (userStatus == UserStatus.PendingEmailVerification) {
            throw CoreException(Error.PENDING_EMAIL_VERIFICATION)
        }
        if (userStatus != UserStatus.Active) {
            throw CoreException(Error.NOT_ACTIVE_USER)
        }
    }

    fun changePassword(newPassword: String) {
        userAccountInformation = userAccountInformation.changePassword(newPassword)
    }

    fun passwordAuthentication(rawPassword: String) {
        userAccountInformation.validatePassword(rawPassword)
    }

    fun updateUserInformation(name: String, profileImage: String) {
        userInformation = userInformation.updateUserInformation(name, profileImage)
    }

    fun setCreator() {
        this.userType = UserType.CreatorUser
    }

    companion object {
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

        fun guest(): User {
            return User(
                uid = -1L,
                userAccountInformation = UserAccountInformation.guestAccountInformation,
                userInformation = UserInformation.guestInformation,
                userType = UserType.Guest,
                userStatus = UserStatus.Active
            )
        }
    }
}
