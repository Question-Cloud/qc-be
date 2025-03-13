package com.eager.questioncloud.application.api.payment.point.implement

import com.eager.questioncloud.application.api.payment.point.event.FailChargePointPaymentEvent
import com.eager.questioncloud.application.listener.payment.FailChargePointPaymentListener
import com.eager.questioncloud.core.domain.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.core.domain.point.enums.ChargePointType
import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment
import com.eager.questioncloud.pg.implement.PGPaymentProcessor
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
internal class FailChargePointPaymentListenerTest {
    @Autowired
    private val chargePointPaymentRepository: ChargePointPaymentRepository? = null

    @Autowired
    private val failChargePointPaymentListener: FailChargePointPaymentListener? = null

    @MockBean
    private val pgPaymentProcessor: PGPaymentProcessor? = null

    @AfterEach
    fun tearDown() {
        chargePointPaymentRepository!!.deleteAllInBatch()
    }

    @Test
    @DisplayName("FailChargePointPaymentEvent를 처리할 수 있다.")
    fun cancelHandler() {
        //given
        val paymentId = UUID.randomUUID().toString()

        val order = ChargePointPayment.createOrder(1L, ChargePointType.PackageA)
        order.approve(paymentId)

        chargePointPaymentRepository!!.save(order)

        BDDMockito.willDoNothing().given(pgPaymentProcessor)!!.cancel(any(), any())

        //when
        failChargePointPaymentListener!!.failHandler(FailChargePointPaymentEvent(order.orderId))

        //then
        val failChargePointPayment = chargePointPaymentRepository.findByOrderId(order.orderId)
        Assertions.assertThat(failChargePointPayment.chargePointPaymentStatus)
            .isEqualTo(ChargePointPaymentStatus.CANCELED)
    }
}