package com.eager.questioncloud.core.exception

class NotVerificationUserException(
    val userId: Long
) : RuntimeException() {
}
