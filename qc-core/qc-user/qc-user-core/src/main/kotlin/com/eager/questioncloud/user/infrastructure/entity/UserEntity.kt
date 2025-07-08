package com.eager.questioncloud.user.infrastructure.entity

import com.eager.questioncloud.user.domain.User
import com.eager.questioncloud.user.enums.UserStatus
import com.eager.questioncloud.user.enums.UserType
import jakarta.persistence.*

@Entity
@Table(name = "user")
class UserEntity(
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id var uid: Long = 0,
    @Embedded var userAccountInformationEntity: UserAccountInformationEntity,
    @Embedded var userInformationEntity: UserInformationEntity,
    @Enumerated(EnumType.STRING) @Column var userType: UserType,
    @Enumerated(EnumType.STRING) @Column var userStatus: UserStatus
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
