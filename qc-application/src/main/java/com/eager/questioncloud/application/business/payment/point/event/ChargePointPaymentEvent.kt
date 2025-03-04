package com.eager.questioncloud.application.business.payment.point.event

import com.eager.questioncloud.core.domain.point.enums.ChargePointType
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment

class ChargePointPaymentEvent(
    val paymentId: String,
    val userId: Long,
    val chargePointType: ChargePointType
) {
    companion object {
        @JvmStatic
        fun from(chargePointPayment: ChargePointPayment): ChargePointPaymentEvent {
            return ChargePointPaymentEvent(
                chargePointPayment.paymentId,
                chargePointPayment.userId,
                chargePointPayment.chargePointType
            )
        }
    }
}
