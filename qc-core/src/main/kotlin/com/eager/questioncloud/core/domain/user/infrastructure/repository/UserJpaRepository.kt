package com.eager.questioncloud.core.domain.user.infrastructure.repository

import com.eager.questioncloud.core.domain.user.enums.AccountType
import com.eager.questioncloud.core.domain.user.infrastructure.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserJpaRepository : JpaRepository<UserEntity, Long> {
    fun existsByUserInformationEntityPhone(phone: String): Boolean

    fun existsByUserInformationEntityEmail(email: String): Boolean

    fun findByUserAccountInformationEntityAccountTypeAndUserAccountInformationEntitySocialUid(
        accountType: AccountType,
        socialUid: String
    ): UserEntity?

    fun findByUserInformationEntityEmail(email: String): Optional<UserEntity>

    fun findByUserInformationEntityPhone(phone: String): Optional<UserEntity>

    fun findByUidIn(ids: List<Long>): List<UserEntity>
}
