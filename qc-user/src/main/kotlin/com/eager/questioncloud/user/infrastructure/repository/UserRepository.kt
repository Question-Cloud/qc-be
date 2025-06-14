package com.eager.questioncloud.user.infrastructure.repository

import com.eager.questioncloud.user.domain.User
import com.eager.questioncloud.user.enums.AccountType

interface UserRepository {
    fun getUserByEmail(email: String): User

    fun getUserByPhone(phone: String): User

    fun getUser(uid: Long): User

    fun save(user: User): User

    fun getSocialUser(accountType: AccountType, socialUid: String): User?

    fun checkDuplicatePhone(phone: String): Boolean

    fun checkDuplicateEmail(email: String): Boolean

    fun checkDuplicateSocialUidAndAccountType(socialUid: String, accountType: AccountType): Boolean

    fun findByUidIn(ids: List<Long>): List<User>
}
