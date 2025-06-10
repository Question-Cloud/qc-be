package com.eager.questioncloud.application.api.common

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
