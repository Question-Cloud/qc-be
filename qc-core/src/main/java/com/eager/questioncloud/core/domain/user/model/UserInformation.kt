package com.eager.questioncloud.core.domain.user.model

import com.eager.questioncloud.core.domain.user.dto.CreateUser

class UserInformation(
    var email: String,
    var phone: String,
    var name: String,
    var profileImage: String? = null,
) {
    fun updateUserInformation(name: String, profileImage: String): UserInformation {
        return UserInformation(this.email, this.phone, name, profileImage)
    }

    companion object {
        @JvmStatic
        var guestInformation: UserInformation = UserInformation("guest", "guest", "guest", "guest")

        @JvmStatic
        fun create(createUser: CreateUser): UserInformation {
            return UserInformation(
                email = createUser.email,
                phone = createUser.phone,
                name = createUser.name
            )
        }
    }
}
