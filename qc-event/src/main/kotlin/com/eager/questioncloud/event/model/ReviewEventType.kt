package com.eager.questioncloud.event.model

enum class ReviewEventType(
    val type: String,
) {
    REGISTER("REGISTER"), MODIFY("MODIFY"), DELETE("DELETE")
}