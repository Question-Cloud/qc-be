package com.eager.questioncloud.core.domain.creator.infrastructure.repository

import com.eager.questioncloud.core.domain.creator.dto.CreatorInformation
import com.eager.questioncloud.core.domain.creator.infrastructure.entity.CreatorEntity.Companion.from
import com.eager.questioncloud.core.domain.creator.infrastructure.entity.QCreatorEntity.creatorEntity
import com.eager.questioncloud.core.domain.creator.infrastructure.entity.QCreatorStatisticsEntity.creatorStatisticsEntity
import com.eager.questioncloud.core.domain.creator.model.Creator
import com.eager.questioncloud.core.domain.user.infrastructure.entity.QUserEntity.userEntity
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class CreatorRepositoryImpl(
    private val creatorJpaRepository: CreatorJpaRepository,
    private val jpaQueryFactory: JPAQueryFactory
) : CreatorRepository {

    override fun existsById(creatorId: Long): Boolean {
        return creatorJpaRepository.existsById(creatorId)
    }

    override fun getCreatorInformation(creatorId: Long): CreatorInformation {
        val creatorInformation = jpaQueryFactory.select(
            Projections.constructor(
                CreatorInformation::class.java,
                creatorEntity.id,
                userEntity.userInformationEntity.name,
                userEntity.userInformationEntity.profileImage,
                creatorEntity.creatorProfileEntity.mainSubject,
                userEntity.userInformationEntity.email,
                creatorStatisticsEntity.salesCount,
                creatorStatisticsEntity.averageRateOfReview,
                creatorEntity.creatorProfileEntity.introduction
            )
        )
            .from(creatorEntity)
            .where(creatorEntity.id.eq(creatorId))
            .leftJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .leftJoin(creatorStatisticsEntity).on(creatorStatisticsEntity.creatorId.eq(creatorEntity.id))
            .fetchFirst()

        if (creatorInformation == null) {
            throw CoreException(Error.NOT_FOUND)
        }

        return creatorInformation
    }

    override fun getCreatorInformation(creatorIds: List<Long>): List<CreatorInformation> {
        return jpaQueryFactory.select(
            Projections.constructor(
                CreatorInformation::class.java,
                creatorEntity.id,
                userEntity.userInformationEntity.name,
                userEntity.userInformationEntity.profileImage,
                creatorEntity.creatorProfileEntity.mainSubject,
                userEntity.userInformationEntity.email,
                creatorStatisticsEntity.salesCount,
                creatorStatisticsEntity.averageRateOfReview,
                creatorEntity.creatorProfileEntity.introduction
            )
        )
            .from(creatorEntity)
            .where(creatorEntity.id.`in`(creatorIds))
            .leftJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .leftJoin(creatorStatisticsEntity).on(creatorStatisticsEntity.creatorId.eq(creatorEntity.id))
            .fetch()
    }

    override fun save(creator: Creator): Creator {
        return creatorJpaRepository.save(from(creator)).toModel()
    }

    override fun deleteAllInBatch() {
        creatorJpaRepository.deleteAllInBatch()
    }
}
