package com.eager.questioncloud.application.api.common

class DefaultResponse(
    val success: Boolean
) {
    companion object {
        fun success(): DefaultResponse {
            return DefaultResponse(true)
        }
    }
}
