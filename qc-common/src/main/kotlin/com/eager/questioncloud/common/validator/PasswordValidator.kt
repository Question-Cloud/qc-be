package com.eager.questioncloud.common.validator

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import java.util.regex.Pattern

object PasswordValidator {
    private const val PASSWORD_REGEX = "^(?!.*\\s).{8,}$"
    private val pattern: Pattern = Pattern.compile(PASSWORD_REGEX)

    @JvmStatic
    fun validate(password: String?) {
        if (password == null) {
            throw throw CoreException(Error.BAD_REQUEST)
        }

        val matcher = pattern.matcher(password)

        if (!matcher.matches()) {
            throw CoreException(Error.BAD_REQUEST)
        }
    }
}
