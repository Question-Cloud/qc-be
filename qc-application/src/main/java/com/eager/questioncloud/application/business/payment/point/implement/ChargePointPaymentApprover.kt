package com.eager.questioncloud.application.business.payment.point.implement

import com.eager.questioncloud.application.message.FailChargePointPaymentMessage
import com.eager.questioncloud.application.message.MessageSender
import com.eager.questioncloud.application.message.MessageType
import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.pg.dto.PGPayment
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ChargePointPaymentApprover(
    private val chargePointPaymentRepository: ChargePointPaymentRepository,
    private val messageSender: MessageSender,
) {
    @Transactional
    fun approve(pgPayment: PGPayment): ChargePointPayment {
        try {
            val chargePointPayment = chargePointPaymentRepository.findByPaymentIdWithLock(pgPayment.paymentId)
            chargePointPayment.validatePayment(pgPayment.amount)
            chargePointPayment.approve(pgPayment.receiptUrl)
            return chargePointPaymentRepository.save(chargePointPayment)
        } catch (coreException: CoreException) {
            throw coreException
        } catch (unknownException: Exception) {
            messageSender.sendMessage(
                MessageType.FAIL_CHARGE_POINT,
                FailChargePointPaymentMessage.create(pgPayment.paymentId)
            )
            throw unknownException
        }
    }
}
