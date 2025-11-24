package com.eager.questioncloud.payment.document

import com.eager.questioncloud.payment.domain.SimpleDiscountHistory
import com.eager.questioncloud.payment.enums.CouponType
import kotlinx.serialization.Serializable

@Serializable
data class SimpleDiscountHistoryDocument(
    val name: String,
    val couponType: CouponType? = null,
    val orderItemId: Long? = null,
    val discountAmount: Int,
) {
    companion object {
        fun from(simpleDiscountHistory: List<SimpleDiscountHistory>): List<SimpleDiscountHistoryDocument> {
            return simpleDiscountHistory.map {
                SimpleDiscountHistoryDocument(
                    name = it.name,
                    couponType = it.couponType,
                    orderItemId = it.orderItemId,
                    discountAmount = it.discountAmount,
                )
            }
        }
    }
    
    fun toModel(): SimpleDiscountHistory {
        return SimpleDiscountHistory(
            name = this.name,
            couponType = this.couponType,
            orderItemId = this.orderItemId,
            discountAmount = this.discountAmount,
        )
    }
}
