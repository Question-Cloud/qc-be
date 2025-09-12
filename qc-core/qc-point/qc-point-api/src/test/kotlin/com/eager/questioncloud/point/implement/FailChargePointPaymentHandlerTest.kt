package com.eager.questioncloud.point.implement

import com.eager.questioncloud.common.message.FailChargePointPaymentMessagePayload
import com.eager.questioncloud.common.pg.PGPayment
import com.eager.questioncloud.common.pg.PGPaymentStatus
import com.eager.questioncloud.common.pg.PaymentAPI
import com.eager.questioncloud.point.domain.ChargePointPayment
import com.eager.questioncloud.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.point.enums.ChargePointType
import com.eager.questioncloud.point.handler.FailChargePointPaymentHandler
import com.eager.questioncloud.point.repository.ChargePointPaymentRepository
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
internal class FailChargePointPaymentHandlerTest(
    @Autowired val chargePointPaymentRepository: ChargePointPaymentRepository,
    @Autowired val failChargePointPaymentHandler: FailChargePointPaymentHandler,
    @Autowired val dbCleaner: DBCleaner,
) {
    @MockBean
    lateinit var paymentAPI: PaymentAPI
    
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }
    
    @Test
    fun `FailChargePointPaymentEvent를 처리할 수 있다`() {
        //given
        val userId = 1L
        val order = ChargePointPayment.createOrder(userId, ChargePointType.PackageA)
        order.prepare(PGPayment("paymentId", order.orderId, order.chargePointType.amount, PGPaymentStatus.DONE))
        order.charge()
        chargePointPaymentRepository.save(order)
        
        doNothing().whenever(paymentAPI).cancel(any())
        
        //when
        failChargePointPaymentHandler.failHandler(FailChargePointPaymentMessagePayload.create(order.orderId))
        
        //then
        val failChargePointPayment = chargePointPaymentRepository.findByOrderId(order.orderId)
        Assertions.assertThat(failChargePointPayment.chargePointPaymentStatus)
            .isEqualTo(ChargePointPaymentStatus.CANCELED)
    }
}