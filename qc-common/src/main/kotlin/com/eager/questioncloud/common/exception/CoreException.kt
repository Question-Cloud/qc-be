package com.eager.questioncloud.common.exception

class CoreException(
    val error: Error
) : RuntimeException(error.message) {
}