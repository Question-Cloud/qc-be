package com.eager.questioncloud.user.register.dto

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.common.validator.EmailValidator
import com.eager.questioncloud.common.validator.PasswordValidator
import com.eager.questioncloud.common.validator.Validatable
import com.eager.questioncloud.user.dto.CreateUser
import com.eager.questioncloud.user.enums.AccountType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.util.StringUtils

class CreateUserRequest(
    val email: String,
    val password: String?,
    val socialRegisterToken: String?,
    val accountType: AccountType,
    @NotBlank val phone: String,
    @Size(min = 2) @NotBlank val name: String
) : Validatable {

    init {
        validate()
    }

    override fun validate() {
        if (accountType == AccountType.EMAIL) {
            EmailValidator.validate(email)
            PasswordValidator.validate(password)
            return
        }
        socialRegisterTokenValidate()
    }

    private fun socialRegisterTokenValidate() {
        if (!StringUtils.hasText(socialRegisterToken)) {
            throw CoreException(Error.BAD_REQUEST)
        }
    }

    fun toCreateUser(): CreateUser {
        return CreateUser(email, password, socialRegisterToken, accountType, phone, name)
    }
}
