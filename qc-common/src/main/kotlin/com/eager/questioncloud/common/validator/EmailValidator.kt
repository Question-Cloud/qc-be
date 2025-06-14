package com.eager.questioncloud.common.validator

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import java.util.regex.Pattern

object EmailValidator {
    const val EMAIL_REGEX: String = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    val pattern: Pattern = Pattern.compile(EMAIL_REGEX)

    @JvmStatic
    fun validate(email: String) {
        val matcher = pattern.matcher(email)
        if (!matcher.matches()) {
            throw CoreException(Error.BAD_REQUEST)
        }
    }
}
