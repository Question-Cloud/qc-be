package com.eager.questioncloud.point.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.common.pg.PGConfirmResult
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
    private val chargePointPaymentFailHandler: ChargePointPaymentFailHandler,
    private val chargePointPaymentRepository: ChargePointPaymentRepository,
    private val chargePointPaymentIdempotentInfoRepository: ChargePointPaymentIdempotentInfoRepository
) {
    @Transactional
    fun postProcess(chargePointPayment: ChargePointPayment, pgConfirmResult: PGConfirmResult): ChargePointPaymentStatus {
        try {
            if (pgConfirmResult is PGConfirmResult.Fail) {
                chargePointPaymentFailHandler.fail(pgConfirmResult.orderId, pgConfirmResult.paymentId)
                return ChargePointPaymentStatus.FAILED
            }
            
            val idempotentInfo = ChargePointPaymentIdempotentInfo(
                orderId = pgConfirmResult.orderId,
                paymentId = pgConfirmResult.paymentId,
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
