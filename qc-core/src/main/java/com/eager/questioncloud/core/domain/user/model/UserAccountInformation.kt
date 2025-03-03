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

    val isSocialAccount: Boolean
        get() = accountType == AccountType.GOOGLE || accountType == AccountType.KAKAO || accountType == AccountType.NAVER

    companion object {
        @JvmStatic
        var guestAccountInformation: UserAccountInformation =
            UserAccountInformation("guest", "guest", AccountType.GUEST)

        @JvmStatic
        fun createEmailAccountInformation(rawPassword: String): UserAccountInformation {
            val encodedPassword = PasswordProcessor.encode(rawPassword)
            return UserAccountInformation(
                password = encodedPassword,
                accountType = AccountType.EMAIL
            )
        }

        @JvmStatic
        fun createSocialAccountInformation(
            socialUid: String,
            socialType: AccountType
        ): UserAccountInformation {
            return UserAccountInformation(socialUid = socialUid, accountType = socialType)
        }
    }
}
