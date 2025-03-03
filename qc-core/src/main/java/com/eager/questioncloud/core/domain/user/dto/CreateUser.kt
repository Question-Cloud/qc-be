package com.eager.questioncloud.core.domain.user.dto

import com.eager.questioncloud.core.domain.user.enums.AccountType

class CreateUser(
    val email: String,
    val password: String?,
    val socialRegisterToken: String?,
    val accountType: AccountType,
    val phone: String,
    val name: String,
)

