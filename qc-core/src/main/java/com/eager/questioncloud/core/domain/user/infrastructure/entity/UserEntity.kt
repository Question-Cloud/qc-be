package com.eager.questioncloud.core.domain.user.infrastructure.entity

import com.eager.questioncloud.core.domain.user.enums.UserStatus
import com.eager.questioncloud.core.domain.user.enums.UserType
import com.eager.questioncloud.core.domain.user.model.User
import jakarta.persistence.*

@Entity
@Table(name = "user")
class UserEntity private constructor(
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id var uid: Long?,
    @Embedded var userAccountInformationEntity: UserAccountInformationEntity,
    @Embedded var userInformationEntity: UserInformationEntity,
    @Enumerated(EnumType.STRING) @Column var userType: UserType?,
    @Enumerated(EnumType.STRING) @Column var userStatus: UserStatus?
) {
    fun toModel(): User {
        return User(
            uid,
            userAccountInformationEntity.toModel(),
            userInformationEntity.toModel(),
            userType,
            userStatus
        )
    }

    companion object {
        @JvmStatic
        fun from(user: User): UserEntity {
            return UserEntity(
                user.uid,
                UserAccountInformationEntity.from(user.userAccountInformation),
                UserInformationEntity.from(user.userInformation),
                user.userType,
                user.userStatus
            )
        }
    }
}
