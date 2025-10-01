package com.eager.questioncloud.payment.repository

import com.eager.questioncloud.payment.domain.CouponInformation

interface CouponInformationRepository {
    fun findById(id: Long): CouponInformation
    
    fun findByIdIn(ids: List<Long>): List<CouponInformation>
    
    fun findByCode(code: String): CouponInformation
    
    fun save(couponInformation: CouponInformation): CouponInformation
    
    fun decreaseCount(couponId: Long): Boolean
    
    fun deleteAllInBatch()
}
