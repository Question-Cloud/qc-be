package com.eager.questioncloud.core.domain.coupon.infrastructure.entity

import com.eager.questioncloud.core.domain.coupon.enums.CouponType
import com.eager.questioncloud.core.domain.coupon.model.Coupon
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "coupon")
class CouponEntity private constructor(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long?,
    @Column var code: String,
    @Column var title: String,
    @Column @Enumerated(EnumType.STRING) var couponType: CouponType,
    @Column var value: Int,
    @Column var remainingCount: Int,
    @Column var endAt: LocalDateTime
) {
    fun toDomain(): Coupon {
        return Coupon(id, code, title, couponType, value, remainingCount, endAt)
    }

    companion object {
        @JvmStatic
        fun from(coupon: Coupon): CouponEntity {
            return CouponEntity(
                coupon.id,
                coupon.code,
                coupon.title,
                coupon.couponType,
                coupon.value,
                coupon.remainingCount,
                coupon.endAt
            )
        }
    }
}
