package com.eager.questioncloud.question.domain

class QuestionMetadata(
    val questionId: Long,
    var sales: Int,
    var reviewCount: Int,
    var totalRate: Int,
    var reviewAverageRate: Double,
) {
    fun updateByNewReview(newRate: Int) {
        this.reviewCount += 1
        this.totalRate += newRate
        val value = totalRate.toDouble() / reviewCount.toDouble()
        this.reviewAverageRate = Math.round(value * 10.0) / 10.0
    }

    fun updateByModifyReview(fluctuationRate: Int) {
        this.totalRate += fluctuationRate
        val value = totalRate.toDouble() / reviewCount.toDouble()
        this.reviewAverageRate = Math.round(value * 10.0) / 10.0
    }

    fun updateByDeleteReview(rate: Int) {
        this.reviewCount -= 1
        this.totalRate -= rate
        val value = totalRate.toDouble() / reviewCount.toDouble()
        this.reviewAverageRate = Math.round(value * 10.0) / 10.0
    }
}