package com.eager.questioncloud.common.event

enum class ReviewEventType(
    val type: String,
) {
    REGISTER("REGISTER"), MODIFY("MODIFY"), DELETE("DELETE")
}