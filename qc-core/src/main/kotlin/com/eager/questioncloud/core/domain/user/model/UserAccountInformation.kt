package com.eager.questioncloud.core.domain.user.model

import com.eager.questioncloud.core.domain.user.enums.AccountType
import com.eager.questioncloud.core.domain.user.implement.PasswordProcessor
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error

class UserAccountInformation(
    var password: String? = null,
    var socialUid: String? = null,
    var accountType: AccountType
) {
    fun changePassword(newRawPassword: String): UserAccountInformation {
        if (accountType != AccountType.EMAIL) {
            throw CoreException(Error.NOT_PASSWORD_SUPPORT_ACCOUNT)
        }
        val newEncodedPassword = PasswordProcessor.encode(newRawPassword)
        return UserAccountInformation(newEncodedPassword, this.socialUid, this.accountType)
    }

    fun validatePassword(rawPassword: String) {
        if (accountType != AccountType.EMAIL) throw CoreException(Error.NOT_PASSWORD_SUPPORT_ACCOUNT)
        if (!PasswordProcessor.matches(rawPassword, password!!)) {
            throw CoreException(Error.FAIL_LOGIN)
        }
    }

    val isSocialAccount: Boolean
        get() = accountType == AccountType.GOOGLE || accountType == AccountType.KAKAO || accountType == AccountType.NAVER

    companion object {
        var guestAccountInformation: UserAccountInformation =
            UserAccountInformation("guest", "guest", AccountType.GUEST)

        fun createEmailAccountInformation(rawPassword: String): UserAccountInformation {
            val encodedPassword = PasswordProcessor.encode(rawPassword)
            return UserAccountInformation(
                password = encodedPassword,
                accountType = AccountType.EMAIL
            )
        }

        fun createSocialAccountInformation(
            socialUid: String,
            socialType: AccountType
        ): UserAccountInformation {
            return UserAccountInformation(socialUid = socialUid, accountType = socialType)
        }
    }
}
