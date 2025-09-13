package com.eager.questioncloud.point.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
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
    private val chargePointPaymentIdempotentInfoRepository: ChargePointPaymentIdempotentInfoRepository
) {
    @Transactional
    fun postProcess(chargePointPayment: ChargePointPayment, pgConfirmResponse: PGConfirmResponse): ChargePointPaymentStatus {
        try {
            if (pgConfirmResponse.status != PGPaymentStatus.DONE) {
                val idempotentInfo = ChargePointPaymentIdempotentInfo(
                    orderId = chargePointPayment.orderId,
                    paymentId = chargePointPayment.paymentId!!,
                    chargePointPaymentStatus = ChargePointPaymentStatus.FAILED
                )
                
                if (chargePointPaymentIdempotentInfoRepository.insert(idempotentInfo)) {
                    chargePointPayment.fail()
                    chargePointPaymentRepository.update(chargePointPayment)
                }
                
                return ChargePointPaymentStatus.FAILED
            }
            
            val idempotentInfo = ChargePointPaymentIdempotentInfo(
                orderId = chargePointPayment.orderId,
                paymentId = chargePointPayment.paymentId!!,
                chargePointPaymentStatus = ChargePointPaymentStatus.CHARGED
            )
            
            if (chargePointPaymentIdempotentInfoRepository.insert(idempotentInfo)) {
                userPointManager.chargePoint(chargePointPayment.userId, chargePointPayment.chargePointType.amount)
                chargePointPayment.charge()
                chargePointPaymentRepository.update(chargePointPayment)
            }
            
            return ChargePointPaymentStatus.CHARGED
        } catch (ex: Exception) {
            throw CoreException(Error.PAYMENT_ERROR)
        }
    }
}
