package com.eager.questioncloud.point.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.pg.PGPayment
import com.eager.questioncloud.common.pg.PGPaymentStatus
import com.eager.questioncloud.point.domain.ChargePointPayment
import com.eager.questioncloud.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.point.enums.ChargePointType
import com.eager.questioncloud.point.repository.ChargePointPaymentRepository
import com.eager.questioncloud.utils.DBCleaner
import org.apache.commons.lang3.RandomStringUtils
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class ChargePointPaymentPreparerTest(
    @Autowired val chargePointPaymentPreparer: ChargePointPaymentPreparer,
    @Autowired val chargePointPaymentRepository: ChargePointPaymentRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }
    
    @Test
    fun `포인트 결제 검증 성공`() {
        //given
        val userId = 1L
        val paymentId = RandomStringUtils.randomAlphanumeric(10)
        val order = chargePointPaymentRepository.save(ChargePointPayment.createOrder(userId, ChargePointType.PackageA))
        val pgPayment = PGPayment(paymentId, order.orderId, ChargePointType.PackageA.amount, PGPaymentStatus.READY)
        
        //when
        chargePointPaymentPreparer.prepare(pgPayment)
        
        //then
        val chargePointPayment = chargePointPaymentRepository.findByOrderId(order.orderId)
        Assertions.assertThat(chargePointPayment.chargePointPaymentStatus)
            .isEqualTo(ChargePointPaymentStatus.PENDING_PG_PAYMENT)
    }
    
    @Test
    fun `결제 금액이 올바르지 않으면 예외가 발생한다`() {
        //given
        val userId = 1L
        val paymentId = RandomStringUtils.randomAlphanumeric(10)
        val order = chargePointPaymentRepository.save(ChargePointPayment.createOrder(userId, ChargePointType.PackageA))
        val wrongPaymentAmount = order.chargePointType.amount - 500
        val pgPayment = PGPayment(paymentId, order.orderId, wrongPaymentAmount, PGPaymentStatus.READY)
        
        //when then
        Assertions.assertThatThrownBy { chargePointPaymentPreparer.prepare(pgPayment) }
            .isInstanceOf(CoreException::class.java)
    }
}