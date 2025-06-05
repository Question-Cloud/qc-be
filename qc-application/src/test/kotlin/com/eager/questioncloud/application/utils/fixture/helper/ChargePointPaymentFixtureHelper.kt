package com.eager.questioncloud.application.utils.fixture.helper

import com.eager.questioncloud.core.domain.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.core.domain.point.enums.ChargePointType
import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment

class ChargePointPaymentFixtureHelper {
    companion object {
        fun createChargePointPayment(
            uid: Long,
            paymentId: String? = null,
            chargePointType: ChargePointType,
            chargePointPaymentStatus: ChargePointPaymentStatus = ChargePointPaymentStatus.ORDERED,
            chargePointPaymentRepository: ChargePointPaymentRepository
        ): ChargePointPayment {
            return chargePointPaymentRepository.save(
                ChargePointPayment(
                    paymentId = paymentId,
                    userId = uid,
                    chargePointType = chargePointType,
                    chargePointPaymentStatus = chargePointPaymentStatus,
                )
            )
        }
    }
}