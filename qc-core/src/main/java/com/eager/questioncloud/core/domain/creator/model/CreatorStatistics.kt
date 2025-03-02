package com.eager.questioncloud.core.domain.creator.model

class CreatorStatistics(
    val creatorId: Long,
    var subscribeCount: Int = 0,
    var salesCount: Int = 0,
    var reviewCount: Int = 0,
    var totalReviewRate: Int = 0,
    var averageRateOfReview: Double = 0.0,
) {
    fun addSaleCount(count: Int) {
        this.salesCount += count
    }

    fun increaseSubscribeCount() {
        this.subscribeCount += 1
    }

    fun decreaseSubscribeCount() {
        this.subscribeCount -= 1
    }

    fun updateReviewStatisticsByRegisteredReview(newRate: Int) {
        this.reviewCount += 1
        this.totalReviewRate += newRate
        val value = totalReviewRate.toDouble() / reviewCount.toDouble()
        this.averageRateOfReview = Math.round(value * 10.0) / 10.0
    }

    fun updateReviewStatisticsByModifiedReview(varianceRate: Int) {
        this.totalReviewRate += varianceRate
        val value = totalReviewRate.toDouble() / reviewCount.toDouble()
        this.averageRateOfReview = Math.round(value * 10.0) / 10.0
    }

    fun updateReviewStatisticsByDeletedReview(rate: Int) {
        this.reviewCount -= 1
        this.totalReviewRate -= rate
        val value = totalReviewRate.toDouble() / reviewCount.toDouble()
        this.averageRateOfReview = Math.round(value * 10.0) / 10.0
    }

    companion object {
        @JvmStatic
        fun create(creatorId: Long): CreatorStatistics {
            return CreatorStatistics(creatorId = creatorId)
        }
    }
}
