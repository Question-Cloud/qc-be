package com.eager.questioncloud.core.domain.user.infrastructure.entity

import com.eager.questioncloud.core.domain.user.enums.AccountType
import com.eager.questioncloud.core.domain.user.model.UserAccountInformation
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Embeddable
class UserAccountInformationEntity private constructor(
    private var password: String? = null,
    private var socialUid: String? = null,
    @Enumerated(EnumType.STRING)
    private var accountType: AccountType
) {
    fun toModel(): UserAccountInformation {
        return UserAccountInformation(password, socialUid, accountType)
    }

    companion object {
        @JvmStatic
        fun from(userAccountInformation: UserAccountInformation): UserAccountInformationEntity {
            return UserAccountInformationEntity(
                userAccountInformation.password,
                userAccountInformation.socialUid,
                userAccountInformation.accountType
            )
        }
    }
}
