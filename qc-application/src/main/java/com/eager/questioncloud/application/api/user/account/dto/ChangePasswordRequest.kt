package com.eager.questioncloud.application.api.user.account.dto

import com.eager.questioncloud.application.validator.PasswordValidator
import com.eager.questioncloud.application.validator.Validatable
import jakarta.validation.constraints.NotBlank

class ChangePasswordRequest(
    @NotBlank val token: String,
    val newPassword: String
) : Validatable {
    init {
        validate()
    }

    override fun validate() {
        PasswordValidator.validate(newPassword)
    }
}
