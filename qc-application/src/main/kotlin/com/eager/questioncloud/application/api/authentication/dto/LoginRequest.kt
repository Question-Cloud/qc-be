package com.eager.questioncloud.application.api.authentication.dto

import com.eager.questioncloud.application.validator.EmailValidator
import com.eager.questioncloud.application.validator.PasswordValidator
import com.eager.questioncloud.application.validator.Validatable

class LoginRequest(val email: String, val password: String) : Validatable {
    init {
        validate()
    }

    override fun validate() {
        EmailValidator.validate(email)
        PasswordValidator.validate(password)
    }
}

