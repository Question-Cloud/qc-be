package com.eager.questioncloud.core.domain.user.dto

import com.eager.questioncloud.core.domain.user.enums.UserType
import com.eager.questioncloud.core.domain.user.model.User

class MyInformation(
    val profileImage: String?,
    val name: String,
    val email: String?,
    val phone: String,
    val userType: UserType,
) {
    companion object {
        @JvmStatic
        fun from(user: User): MyInformation {
            return MyInformation(
                user.userInformation.profileImage,
                user.userInformation.name,
                user.userInformation.email,
                user.userInformation.phone,
                user.userType!!
            )
        }
    }
}
