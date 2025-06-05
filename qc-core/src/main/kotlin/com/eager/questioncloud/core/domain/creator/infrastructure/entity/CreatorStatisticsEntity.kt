package com.eager.questioncloud.core.domain.creator.infrastructure.entity

import com.eager.questioncloud.core.common.BaseCustomIdEntity
import com.eager.questioncloud.core.domain.creator.model.CreatorStatistics
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "creator_statistics")
class CreatorStatisticsEntity(
    @Id var creatorId: Long,
    @Column var salesCount: Int,
    @Column var reviewCount: Int,
    @Column var totalReviewRate: Int,
    @Column var averageRateOfReview: Double,
    isNewEntity: Boolean
) : BaseCustomIdEntity<Long>(isNewEntity) {
    fun toModel(): CreatorStatistics {
        return CreatorStatistics(
            creatorId,
            salesCount,
            reviewCount,
            totalReviewRate,
            averageRateOfReview
        )
    }

    companion object {
        fun createNewEntity(creatorStatistics: CreatorStatistics): CreatorStatisticsEntity {
            return CreatorStatisticsEntity(
                creatorStatistics.creatorId,
                creatorStatistics.salesCount,
                creatorStatistics.reviewCount,
                creatorStatistics.totalReviewRate,
                creatorStatistics.averageRateOfReview,
                true
            )
        }

        fun fromExisting(creatorStatistics: CreatorStatistics): CreatorStatisticsEntity {
            return CreatorStatisticsEntity(
                creatorStatistics.creatorId,
                creatorStatistics.salesCount,
                creatorStatistics.reviewCount,
                creatorStatistics.totalReviewRate,
                creatorStatistics.averageRateOfReview,
                false
            )
        }
    }

    override fun getId(): Long {
        return creatorId
    }
}
