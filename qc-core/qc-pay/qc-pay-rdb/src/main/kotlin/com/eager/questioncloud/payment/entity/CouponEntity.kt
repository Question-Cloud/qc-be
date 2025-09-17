package com.eager.questioncloud.payment.entity

import com.eager.questioncloud.payment.domain.Coupon
import com.eager.questioncloud.payment.enums.CouponType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "coupon")
class CouponEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long = 0,
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
