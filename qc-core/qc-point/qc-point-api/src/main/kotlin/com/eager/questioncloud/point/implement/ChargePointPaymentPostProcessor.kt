package com.eager.questioncloud.point.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.common.message.FailChargePointPaymentMessagePayload
import com.eager.questioncloud.common.pg.domain.PGPayment
import com.eager.questioncloud.common.pg.domain.PGPaymentStatus
import com.eager.questioncloud.point.domain.ChargePointPayment
import com.eager.questioncloud.point.repository.ChargePointPaymentRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ChargePointPaymentPostProcessor(
    private val userPointManager: UserPointManager,
    private val chargePointPaymentRepository: ChargePointPaymentRepository,
    private val failChargePointPaymentMessageSender: FailChargePointPaymentMessageSender,
) {
    @Transactional
    fun postProcess(chargePointPayment: ChargePointPayment, pgPayment: PGPayment) {
        if (pgPayment.status != PGPaymentStatus.DONE) {
            chargePointPayment.fail()
            chargePointPaymentRepository.update(chargePointPayment)
            return;
        }
        
        runCatching {
            userPointManager.chargePoint(chargePointPayment.userId, chargePointPayment.chargePointType.amount)
            chargePointPayment.charge()
            chargePointPaymentRepository.update(chargePointPayment)
        }.onFailure {
            failChargePointPaymentMessageSender.publishMessage(FailChargePointPaymentMessagePayload.create(chargePointPayment.orderId))
            throw CoreException(Error.PAYMENT_ERROR)
        }
    }
}
