package com.eager.questioncloud.point.implement

import com.eager.questioncloud.common.event.FailChargePointPaymentMessage
import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
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
    fun chargeUserPoint(chargePointPayment: ChargePointPayment) {
        runCatching {
            userPointManager.chargePoint(chargePointPayment.userId, chargePointPayment.chargePointType.amount)
            chargePointPayment.charge()
            chargePointPaymentRepository.update(chargePointPayment)
        }.onFailure {
            failChargePointPaymentMessageSender.publishMessage(FailChargePointPaymentMessage.create(chargePointPayment.orderId))
            throw CoreException(Error.PAYMENT_ERROR)
        }
    }
}
