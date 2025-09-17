package com.eager.questioncloud.user.entity

import com.eager.questioncloud.user.domain.UserAccountInformation
import com.eager.questioncloud.user.enums.AccountType
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Embeddable
class UserAccountInformationEntity(
    private var password: String? = null,
    private var socialUid: String? = null,
    @Enumerated(EnumType.STRING)
    private var accountType: AccountType
) {
    fun toModel(): UserAccountInformation {
        return UserAccountInformation(password, socialUid, accountType)
    }
    
    companion object {
        fun from(userAccountInformation: UserAccountInformation): UserAccountInformationEntity {
            return UserAccountInformationEntity(
                userAccountInformation.password,
                userAccountInformation.socialUid,
                userAccountInformation.accountType
            )
        }
    }
}
