package com.eager.questioncloud.core.domain.point.model

import com.eager.questioncloud.core.domain.point.enums.ChargePointType
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment.Companion.order
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import com.eager.questioncloud.core.exception.InvalidPaymentException
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.*

internal class ChargePointPaymentTest {
    @Test
    @DisplayName("포인트 충전 결제 검증을 할 수 있다.")
    fun validatePayment() {
        //given
        val paymentId = UUID.randomUUID().toString()
        val userId = 1L
        val chargePointType = ChargePointType.PackageC
        val chargePointPayment = order(paymentId, userId, chargePointType)
        val paidAmount = chargePointType.amount

        // when then
        Assertions.assertThatNoException().isThrownBy {
            chargePointPayment.validatePayment(
                paidAmount
            )
        }
    }

    @Test
    @DisplayName("결제 금액이 올바르지 않을 경우 결제 검증에 실패한다.")
    fun cancelValidatePaymentWhenInCorrectAmount() {
        //given
        val paymentId = UUID.randomUUID().toString()
        val userId = 1L
        val chargePointType = ChargePointType.PackageC
        val chargePointPayment = order(paymentId, userId, chargePointType)
        val wrongAmount = 100

        //when then
        Assertions.assertThatThrownBy { chargePointPayment.validatePayment(wrongAmount) }
            .isInstanceOf(InvalidPaymentException::class.java)
    }

    @Test
    @DisplayName("이미 처리 된 결제인 경우 검증에 실패한다.")
    fun cancelValidatePaymentWhenAlreadyApproved() {
        //given
        val paymentId1 = UUID.randomUUID().toString()
        val userId = 1L
        val chargePointType = ChargePointType.PackageC
        val chargePointPayment1 = order(paymentId1, userId, chargePointType)
        chargePointPayment1.approve("approved")

        val paymentId2 = UUID.randomUUID().toString()
        val chargePointPayment2 = order(paymentId2, userId, chargePointType)
        chargePointPayment2.cancel()

        val paidAmount = chargePointType.amount

        //when then
        Assertions.assertThatThrownBy { chargePointPayment1.validatePayment(paidAmount) }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.ALREADY_PROCESSED_PAYMENT)

        Assertions.assertThatThrownBy { chargePointPayment2.validatePayment(paidAmount) }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.ALREADY_PROCESSED_PAYMENT)
    }
}