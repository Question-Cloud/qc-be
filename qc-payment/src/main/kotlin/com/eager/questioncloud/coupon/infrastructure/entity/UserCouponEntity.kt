package com.eager.questioncloud.coupon.infrastructure.entity

import com.eager.questioncloud.coupon.domain.UserCoupon
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "user_coupon")
class UserCouponEntity private constructor(
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id var id: Long = 0,
    @Column var userId: Long,
    @Column var couponId: Long,
    @Column var isUsed: Boolean,
    @Column var createdAt: LocalDateTime,
    @Column var endAt: LocalDateTime
) {
    fun toModel(): UserCoupon {
        return UserCoupon(id, userId, couponId, isUsed, createdAt, endAt)
    }

    companion object {
        fun from(userCoupon: UserCoupon): UserCouponEntity {
            return UserCouponEntity(
                userCoupon.id,
                userCoupon.userId,
                userCoupon.couponId,
                userCoupon.isUsed,
                userCoupon.createdAt,
                userCoupon.endAt
            )
        }
    }
}
