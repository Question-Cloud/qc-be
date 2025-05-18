package com.eager.questioncloud.core.domain.point.infrastructure.repository

import com.eager.questioncloud.core.domain.point.infrastructure.entity.QUserPointEntity.userPointEntity
import com.eager.questioncloud.core.domain.point.infrastructure.entity.UserPointEntity.Companion.from
import com.eager.questioncloud.core.domain.point.model.UserPoint
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class UserPointRepositoryImpl(
    private val userPointJpaRepository: UserPointJpaRepository,
    private val jpaQueryFactory: JPAQueryFactory,
) : UserPointRepository {

    override fun getUserPoint(userId: Long): UserPoint {
        return userPointJpaRepository.findById(userId)
            .orElseThrow { CoreException(Error.NOT_FOUND) }
            .toModel()
    }

    @Transactional
    override fun usePoint(userId: Long, amount: Int): Boolean {
        return jpaQueryFactory.update(userPointEntity)
            .set(userPointEntity.point, userPointEntity.point.subtract(amount))
            .where(
                userPointEntity.userId.eq(userId),
                userPointEntity.point.goe(amount)
            )
            .execute() == 1L
    }

    @Transactional
    override fun chargePoint(userId: Long, amount: Int) {
        jpaQueryFactory.update(userPointEntity)
            .set(userPointEntity.point, userPointEntity.point.add(amount))
            .where(userPointEntity.userId.eq(userId))
            .execute()
    }

    override fun save(userPoint: UserPoint): UserPoint {
        return userPointJpaRepository.save(from(userPoint)).toModel()
    }

    override fun deleteAllInBatch() {
        userPointJpaRepository.deleteAllInBatch()
    }
}
