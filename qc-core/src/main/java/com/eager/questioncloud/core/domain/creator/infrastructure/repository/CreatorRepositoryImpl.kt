package com.eager.questioncloud.core.domain.creator.infrastructure.repository

import com.eager.questioncloud.core.domain.creator.dto.CreatorProfile
import com.eager.questioncloud.core.domain.creator.infrastructure.entity.CreatorEntity.Companion.from
import com.eager.questioncloud.core.domain.creator.infrastructure.entity.QCreatorEntity.creatorEntity
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

    override fun getCreatorProfile(creatorId: Long): CreatorProfile {
        return jpaQueryFactory.select(
            Projections.constructor(
                CreatorProfile::class.java,
                creatorEntity.id,
                userEntity.userInformationEntity.name,
                userEntity.userInformationEntity.profileImage,
                creatorEntity.mainSubject,
                userEntity.userInformationEntity.email,
                creatorEntity.introduction
            )
        )
            .from(creatorEntity)
            .where(creatorEntity.id.eq(creatorId))
            .leftJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .fetchFirst() ?: throw CoreException(Error.NOT_FOUND)
    }

    override fun getCreatorProfile(creatorIds: List<Long>): Map<Long, CreatorProfile> {
        return jpaQueryFactory.select(
            Projections.constructor(
                CreatorProfile::class.java,
                creatorEntity.id,
                userEntity.userInformationEntity.name,
                userEntity.userInformationEntity.profileImage,
                creatorEntity.mainSubject,
                userEntity.userInformationEntity.email,
                creatorEntity.introduction
            )
        )
            .from(creatorEntity)
            .where(creatorEntity.id.`in`(creatorIds))
            .leftJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .fetch()
            .associateBy { it.creatorId }
    }

    override fun save(creator: Creator): Creator {
        return creatorJpaRepository.save(from(creator)).toModel()
    }

    override fun deleteAllInBatch() {
        creatorJpaRepository.deleteAllInBatch()
    }
}
