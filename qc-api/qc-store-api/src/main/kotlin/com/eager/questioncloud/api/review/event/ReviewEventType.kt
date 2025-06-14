package com.eager.questioncloud.api.review.event

enum class ReviewEventType(
    val type: String,
) {
    REGISTER("REGISTER"), MODIFY("MODIFY"), DELETE("DELETE")
}