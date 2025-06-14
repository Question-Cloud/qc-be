package com.eager.questioncloud.point.dto

import com.eager.questioncloud.point.domain.ChargePointPayment
import com.eager.questioncloud.point.enums.ChargePointType
import java.time.LocalDateTime
import java.util.stream.Collectors

class ChargePointPaymentHistory(
    val orderId: String,
    val chargePointType: ChargePointType,
    val paidAt: LocalDateTime?
) {
    companion object {
        fun from(chargePointPayments: List<ChargePointPayment>): List<ChargePointPaymentHistory> {
            return chargePointPayments.stream()
                .map { chargePointPayment ->
                    from(
                        chargePointPayment
                    )
                }
                .collect(Collectors.toList())
        }

        fun from(chargePointPayment: ChargePointPayment): ChargePointPaymentHistory {
            return ChargePointPaymentHistory(
                chargePointPayment.orderId,
                chargePointPayment.chargePointType,
                chargePointPayment.requestAt
            )
        }
    }
}
