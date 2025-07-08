package com.eager.questioncloud.user.dto

import com.eager.questioncloud.user.domain.User
import com.eager.questioncloud.user.enums.UserType

class MyInformation(
    val profileImage: String?,
    val name: String,
    val email: String?,
    val phone: String,
    val userType: UserType,
) {
    companion object {
        fun from(user: User): MyInformation {
            return MyInformation(
                user.userInformation.profileImage,
                user.userInformation.name,
                user.userInformation.email,
                user.userInformation.phone,
                user.userType
            )
        }
    }
}
