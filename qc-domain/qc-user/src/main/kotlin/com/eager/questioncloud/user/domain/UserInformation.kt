package com.eager.questioncloud.user.domain

import com.eager.questioncloud.user.dto.CreateUser

class UserInformation(
    var email: String,
    var phone: String,
    var name: String,
    var profileImage: String = "default",
) {
    fun updateUserInformation(name: String, profileImage: String): UserInformation {
        return UserInformation(this.email, this.phone, name, profileImage)
    }

    companion object {
        var guestInformation: UserInformation =
            UserInformation("guest", "guest", "guest", "guest")

        fun create(createUser: CreateUser): UserInformation {
            return UserInformation(
                email = createUser.email,
                phone = createUser.phone,
                name = createUser.name
            )
        }
    }
}
