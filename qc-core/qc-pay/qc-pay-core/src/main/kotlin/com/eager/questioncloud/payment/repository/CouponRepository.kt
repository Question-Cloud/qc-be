package com.eager.questioncloud.payment.repository

import com.eager.questioncloud.payment.domain.Coupon
import org.springframework.stereotype.Repository

@Repository
interface CouponRepository {
    fun findById(id: Long): Coupon
    
    fun findByCode(code: String): Coupon
    
    fun save(coupon: Coupon): Coupon
    
    fun decreaseCount(couponId: Long): Boolean
    
    fun deleteAllInBatch()
}
