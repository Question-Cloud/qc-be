package com.eager.questioncloud.user.account.dto

import com.eager.questioncloud.common.validator.PasswordValidator
import com.eager.questioncloud.common.validator.Validatable
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
