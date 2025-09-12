package com.eager.questioncloud.point.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.common.message.FailChargePointPaymentMessagePayload
import com.eager.questioncloud.common.pg.PGConfirmResponse
import com.eager.questioncloud.common.pg.PGPaymentStatus
import com.eager.questioncloud.point.domain.ChargePointPayment
import com.eager.questioncloud.point.domain.ChargePointPaymentIdempotentInfo
import com.eager.questioncloud.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.point.repository.ChargePointPaymentIdempotentInfoRepository
import com.eager.questioncloud.point.repository.ChargePointPaymentRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ChargePointPaymentPostProcessor(
    private val userPointManager: UserPointManager,
    private val chargePointPaymentRepository: ChargePointPaymentRepository,
    private val failChargePointPaymentMessageSender: FailChargePointPaymentMessageSender,
    private val chargePointPaymentIdempotentInfoRepository: ChargePointPaymentIdempotentInfoRepository
) {
    @Transactional
    fun postProcess(chargePointPayment: ChargePointPayment, pgConfirmResponse: PGConfirmResponse): ChargePointPaymentStatus {
        if (pgConfirmResponse.status != PGPaymentStatus.DONE) {
            val idempotentInfo = ChargePointPaymentIdempotentInfo(
                orderId = chargePointPayment.orderId,
                paymentId = chargePointPayment.paymentId!!,
                chargePointPaymentStatus = ChargePointPaymentStatus.FAILED
            )
            
            if (chargePointPaymentIdempotentInfoRepository.save(idempotentInfo)) {
                chargePointPayment.fail()
                chargePointPaymentRepository.update(chargePointPayment)
            }
            
            return ChargePointPaymentStatus.FAILED
        }
        
        return runCatching {
            val idempotentInfo = ChargePointPaymentIdempotentInfo(
                orderId = chargePointPayment.orderId,
                paymentId = chargePointPayment.paymentId!!,
                chargePointPaymentStatus = ChargePointPaymentStatus.CHARGED
            )
            
            if (chargePointPaymentIdempotentInfoRepository.save(idempotentInfo)) {
                userPointManager.chargePoint(chargePointPayment.userId, chargePointPayment.chargePointType.amount)
                chargePointPayment.charge()
                chargePointPaymentRepository.update(chargePointPayment)
            }
            
            ChargePointPaymentStatus.CHARGED
        }.getOrElse {
            failChargePointPaymentMessageSender.publishMessage(FailChargePointPaymentMessagePayload.create(chargePointPayment.orderId))
            throw CoreException(Error.PAYMENT_ERROR)
        }
    }
}
