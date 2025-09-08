package com.eager.questioncloud.point.repository

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.point.domain.UserPoint
import com.eager.questioncloud.point.entity.QUserPointEntity.userPointEntity
import com.eager.questioncloud.point.entity.UserPointEntity
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
        return userPointJpaRepository.save(
            UserPointEntity.createNewEntity(
                userPoint
            )
        ).toModel()
    }
    
    override fun deleteAllInBatch() {
        userPointJpaRepository.deleteAllInBatch()
    }
}
