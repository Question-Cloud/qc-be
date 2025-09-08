package com.eager.questioncloud.point.entity

import com.eager.questioncloud.common.entity.BaseCustomIdEntity
import com.eager.questioncloud.point.domain.UserPoint
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "user_point")
class UserPointEntity(
    @Id var userId: Long,
    @Column var point: Int,
    isNewEntity: Boolean
) : BaseCustomIdEntity<Long>(isNewEntity) {
    fun toModel(): UserPoint {
        return UserPoint(userId, point)
    }
    
    companion object {
        fun createNewEntity(userPoint: UserPoint): UserPointEntity {
            return UserPointEntity(
                userPoint.userId,
                userPoint.point,
                true
            )
        }
    }
    
    override fun getId(): Long {
        return userId
    }
}
