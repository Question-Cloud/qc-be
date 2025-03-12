package com.eager.questioncloud.application.business.payment.point.implement

import com.eager.questioncloud.application.api.payment.point.event.FailChargePointPaymentEvent
import com.eager.questioncloud.application.listener.payment.FailChargePointPaymentListener
import com.eager.questioncloud.core.domain.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.core.domain.point.enums.ChargePointType
import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment.Companion.order
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
    @Throws(
        Exception::class
    )
    fun cancelHandler() {
        //given
        val paymentId = UUID.randomUUID().toString()
        chargePointPaymentRepository!!.save(order(paymentId, 1L, ChargePointType.PackageA))

        BDDMockito.willDoNothing().given(pgPaymentProcessor)!!.cancel(any())

        //when
        failChargePointPaymentListener!!.failHandler(FailChargePointPaymentEvent(paymentId))

        //then
        val failChargePointPayment = chargePointPaymentRepository.findByPaymentId(paymentId)
        Assertions.assertThat(failChargePointPayment.chargePointPaymentStatus)
            .isEqualTo(ChargePointPaymentStatus.CANCELED)
    }
}