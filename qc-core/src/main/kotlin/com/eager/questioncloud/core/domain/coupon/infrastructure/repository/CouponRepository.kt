package com.eager.questioncloud.core.domain.coupon.infrastructure.repository

import com.eager.questioncloud.core.domain.coupon.model.Coupon
import org.springframework.stereotype.Repository

@Repository
interface CouponRepository {
    fun findById(id: Long): Coupon

    fun findByCode(code: String): Coupon

    fun save(coupon: Coupon): Coupon

    fun decreaseCount(couponId: Long): Boolean

    fun deleteAllInBatch()
}
