package com.eager.questioncloud.application.api.common

class DefaultResponse(
    val success: Boolean
) {
    companion object {
        @JvmStatic
        fun success(): DefaultResponse {
            return DefaultResponse(true)
        }
    }
}
