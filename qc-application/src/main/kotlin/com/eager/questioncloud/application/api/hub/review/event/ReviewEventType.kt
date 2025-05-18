package com.eager.questioncloud.application.api.hub.review.event

enum class ReviewEventType(
    val type: String,
) {
    REGISTER("REGISTER"), MODIFY("MODIFY"), DELETE("DELETE")
}