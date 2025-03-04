package com.eager.questioncloud.application.validator

import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import java.util.regex.Pattern

object PasswordValidator {
    private const val PASSWORD_REGEX = "^(?!.*\\s).{8,}$"
    private val pattern: Pattern = Pattern.compile(PASSWORD_REGEX)

    @JvmStatic
    fun validate(password: String) {
        val matcher = pattern.matcher(password)
        if (!matcher.matches()) {
            throw CoreException(Error.BAD_REQUEST)
        }
    }
}
