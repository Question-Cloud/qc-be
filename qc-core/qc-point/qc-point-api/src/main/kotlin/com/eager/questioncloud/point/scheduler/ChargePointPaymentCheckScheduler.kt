package com.eager.questioncloud.point.scheduler

import com.eager.questioncloud.common.pg.PGConfirmRequest
import com.eager.questioncloud.point.implement.ChargePointPaymentPGProcessor
import com.eager.questioncloud.point.implement.ChargePointPaymentPostProcessor
import com.eager.questioncloud.point.repository.ChargePointPaymentRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ChargePointPaymentCheckScheduler(
    private val chargePointPaymentRepository: ChargePointPaymentRepository,
    private val chargePointPaymentPGProcessor: ChargePointPaymentPGProcessor,
    private val chargePointPaymentPostProcessor: ChargePointPaymentPostProcessor,
) {
    @Scheduled(fixedRate = 60000)
    fun chargePointPaymentCheckScheduler() {
        val targets = chargePointPaymentRepository.getPendingPayments()
        
        targets.forEach { payment ->
            val pgConfirmRequest = PGConfirmRequest(payment.paymentId!!, payment.orderId, payment.chargePointType.amount)
            val confirmResponse = chargePointPaymentPGProcessor.confirm(pgConfirmRequest)
            chargePointPaymentPostProcessor.postProcess(payment, confirmResponse)
        }
    }
}