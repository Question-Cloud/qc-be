package com.eager.questioncloud.core.domain.user.infrastructure.repository

import com.eager.questioncloud.core.domain.user.dto.UserWithCreator
import com.eager.questioncloud.core.domain.user.enums.AccountType
import com.eager.questioncloud.core.domain.user.model.User

interface UserRepository {
    fun getUserByEmail(email: String): User

    fun getUserByPhone(phone: String): User

    fun getUser(uid: Long): User

    fun getUserWithCreator(uid: Long): UserWithCreator

    fun save(user: User): User

    fun getSocialUser(accountType: AccountType, socialUid: String): User?

    fun checkDuplicatePhone(phone: String): Boolean

    fun checkDuplicateEmail(email: String): Boolean

    fun checkDuplicateSocialUidAndAccountType(socialUid: String, accountType: AccountType): Boolean

    fun findByUidIn(ids: List<Long>): List<User>
}
