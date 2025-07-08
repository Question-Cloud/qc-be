package com.eager.questioncloud.user.authentication.dto

import com.eager.questioncloud.common.validator.EmailValidator
import com.eager.questioncloud.common.validator.PasswordValidator
import com.eager.questioncloud.common.validator.Validatable

class LoginRequest(val email: String, val password: String) : Validatable {
    init {
        validate()
    }

    override fun validate() {
        EmailValidator.validate(email)
        PasswordValidator.validate(password)
    }
}

