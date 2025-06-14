package com.eager.questioncloud.common.dto

import com.fasterxml.jackson.annotation.JsonProperty

class DefaultResponse(
    @JsonProperty("success")
    val success: Boolean
) {
    companion object {
        fun success(): DefaultResponse {
            return DefaultResponse(true)
        }
    }
}
