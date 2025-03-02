package com.eager.questioncloud.core.domain.point.infrastructure.entity

import com.eager.questioncloud.core.domain.point.model.UserPoint
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "user_point")
class UserPointEntity private constructor(
    @Id var userId: Long,
    @Column var point: Int
) {
    fun toModel(): UserPoint {
        return UserPoint(userId, point)
    }

    companion object {
        @JvmStatic
        fun from(userPoint: UserPoint): UserPointEntity {
            return UserPointEntity(userPoint.userId, userPoint.point)
        }
    }
}
