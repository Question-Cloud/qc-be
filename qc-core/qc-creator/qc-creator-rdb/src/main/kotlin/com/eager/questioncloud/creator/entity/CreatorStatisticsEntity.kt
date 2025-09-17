package com.eager.questioncloud.creator.entity

import com.eager.questioncloud.common.entity.BaseCustomIdEntity
import com.eager.questioncloud.creator.domain.CreatorStatistics
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
    @Column var subscriberCount: Int,
    isNewEntity: Boolean
) : BaseCustomIdEntity<Long>(isNewEntity) {
    fun toModel(): CreatorStatistics {
        return CreatorStatistics(
            creatorId,
            salesCount,
            reviewCount,
            totalReviewRate,
            averageRateOfReview,
            subscriberCount
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
                creatorStatistics.subscriberCount,
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
                creatorStatistics.subscriberCount,
                false
            )
        }
    }
    
    override fun getId(): Long {
        return creatorId
    }
}
