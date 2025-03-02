package com.eager.questioncloud.core.domain.creator.infrastructure.entity

import com.eager.questioncloud.core.domain.creator.model.CreatorStatistics
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "creator_statistics")
class CreatorStatisticsEntity private constructor(
    @Id var creatorId: Long?,
    @Column var subscribeCount: Int,
    @Column var salesCount: Int,
    @Column var reviewCount: Int,
    @Column var totalReviewRate: Int,
    @Column var averageRateOfReview: Double
) {
    fun toModel(): CreatorStatistics {
        return CreatorStatistics(
            creatorId!!,
            subscribeCount,
            salesCount,
            reviewCount,
            totalReviewRate,
            averageRateOfReview
        )
    }

    companion object {
        @JvmStatic
        fun from(creatorStatistics: CreatorStatistics): CreatorStatisticsEntity {
            return CreatorStatisticsEntity(
                creatorStatistics.creatorId,
                creatorStatistics.subscribeCount,
                creatorStatistics.salesCount,
                creatorStatistics.reviewCount,
                creatorStatistics.totalReviewRate,
                creatorStatistics.averageRateOfReview
            )
        }
    }
}
