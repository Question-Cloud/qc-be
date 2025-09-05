package com.eager.questioncloud.point.implement

import com.eager.questioncloud.event.model.FailChargePointPaymentMessage
import com.eager.questioncloud.pg.toss.TossPaymentAPI
import com.eager.questioncloud.point.domain.ChargePointPayment
import com.eager.questioncloud.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.point.enums.ChargePointType
import com.eager.questioncloud.point.infrastructure.repository.ChargePointPaymentRepository
import com.eager.questioncloud.point.listener.FailChargePointPaymentListener
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
internal class FailChargePointPaymentListenerTest(
    @Autowired val chargePointPaymentRepository: ChargePointPaymentRepository,
    @Autowired val failChargePointPaymentListener: FailChargePointPaymentListener,
    @Autowired val dbCleaner: DBCleaner,
) {
    @MockBean
    lateinit var tossPaymentAPI: TossPaymentAPI
    
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }
    
    @Test
    fun `FailChargePointPaymentEvent를 처리할 수 있다`() {
        //given
        val userId = 1L
        val order = ChargePointPayment.createOrder(userId, ChargePointType.PackageA)
        order.prepare("paymentId")
        order.charge()
        chargePointPaymentRepository.save(order)
        
        doNothing().whenever(tossPaymentAPI).cancel(any(), any())
        
        //when
        failChargePointPaymentListener.failHandler(FailChargePointPaymentMessage.create(order.orderId))
        
        //then
        val failChargePointPayment = chargePointPaymentRepository.findByOrderId(order.orderId)
        Assertions.assertThat(failChargePointPayment.chargePointPaymentStatus)
            .isEqualTo(ChargePointPaymentStatus.CANCELED)
    }
}