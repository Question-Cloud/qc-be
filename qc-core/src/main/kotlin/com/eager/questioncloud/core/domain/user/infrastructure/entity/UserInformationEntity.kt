package com.eager.questioncloud.core.domain.user.infrastructure.entity

import com.eager.questioncloud.core.domain.user.model.UserInformation
import jakarta.persistence.Embeddable

@Embeddable
class UserInformationEntity private constructor(
    private var email: String,
    private var phone: String,
    private var name: String,
    private var profileImage: String? = null
) {
    fun toModel(): UserInformation {
        return UserInformation(email, phone, name, profileImage)
    }

    companion object {
        fun from(userInformation: UserInformation): UserInformationEntity {
            return UserInformationEntity(
                userInformation.email,
                userInformation.phone,
                userInformation.name,
                userInformation.profileImage
            )
        }
    }
}
