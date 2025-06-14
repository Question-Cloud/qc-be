package com.eager.questioncloud.user.exception

class NotVerificationUserException(
    val userId: Long
) : RuntimeException() {
}
