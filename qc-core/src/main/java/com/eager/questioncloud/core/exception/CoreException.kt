package com.eager.questioncloud.core.exception

class CoreException(
    val error: Error
) : RuntimeException() {
}