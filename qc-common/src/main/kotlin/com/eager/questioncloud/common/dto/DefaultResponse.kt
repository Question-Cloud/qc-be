package com.eager.questioncloud.common.dto

class DefaultResponse(
    val success: Boolean
) {
    companion object {
        fun success(): DefaultResponse {
            return DefaultResponse(true)
        }
    }
}
