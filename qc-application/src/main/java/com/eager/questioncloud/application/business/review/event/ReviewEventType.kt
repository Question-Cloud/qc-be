package com.eager.questioncloud.application.business.review.event

enum class ReviewEventType(
    val type: String,
) {
    REGISTER("REGISTER"), MODIFY("MODIFY"), DELETE("DELETE")
}