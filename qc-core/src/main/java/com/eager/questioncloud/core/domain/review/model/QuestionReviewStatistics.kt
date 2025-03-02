package com.eager.questioncloud.core.domain.review.model

class QuestionReviewStatistics(
    val questionId: Long,
    var reviewCount: Int = 0,
    var totalRate: Int = 0,
    var averageRate: Double = 0.0,
) {
    fun updateByNewReview(newRate: Int) {
        this.reviewCount += 1
        this.totalRate += newRate
        val value = totalRate.toDouble() / reviewCount.toDouble()
        this.averageRate = Math.round(value * 10.0) / 10.0
    }

    fun updateByModifyReview(fluctuationRate: Int) {
        this.totalRate += fluctuationRate
        val value = totalRate.toDouble() / reviewCount.toDouble()
        this.averageRate = Math.round(value * 10.0) / 10.0
    }

    fun updateByDeleteReview(rate: Int) {
        this.reviewCount -= 1
        this.totalRate -= rate
        val value = totalRate.toDouble() / reviewCount.toDouble()
        this.averageRate = Math.round(value * 10.0) / 10.0
    }

    companion object {
        @JvmStatic
        fun create(questionId: Long): QuestionReviewStatistics {
            return QuestionReviewStatistics(questionId = questionId)
        }
    }
}
