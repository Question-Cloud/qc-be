package com.eager.questioncloud.review.command

data class DeleteReviewCommand(
    val reviewId: Long,
    val reviewerId: Long,
)
