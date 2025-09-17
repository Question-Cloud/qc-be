package com.eager.questioncloud.user.repository

import com.eager.questioncloud.user.entity.UserEntity
import com.eager.questioncloud.user.enums.AccountType
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
