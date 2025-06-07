package com.eager.questioncloud.application.api.payment.point.implement

import com.eager.questioncloud.application.api.payment.point.event.FailChargePointPaymentEvent
import com.eager.questioncloud.application.listener.payment.FailChargePointPaymentListener
import com.eager.questioncloud.application.utils.DBCleaner
import com.eager.questioncloud.application.utils.fixture.helper.ChargePointPaymentFixtureHelper
import com.eager.questioncloud.core.domain.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.core.domain.point.enums.ChargePointType
import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository
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
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
internal class FailChargePointPaymentListenerTest(
    @Autowired val chargePointPaymentRepository: ChargePointPaymentRepository,
    @Autowired val failChargePointPaymentListener: FailChargePointPaymentListener,
    @Autowired val dbCleaner: DBCleaner,
) {
    @MockBean
    lateinit var chargePointPaymentPGProcessor: ChargePointPaymentPGProcessor

    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `FailChargePointPaymentEvent를 처리할 수 있다`() {
        //given
        val paymentId = UUID.randomUUID().toString()
        val order = ChargePointPaymentFixtureHelper.createChargePointPayment(
            uid = 1L,
            paymentId = paymentId,
            chargePointType = ChargePointType.PackageA,
            chargePointPaymentRepository = chargePointPaymentRepository,
            chargePointPaymentStatus = ChargePointPaymentStatus.CHARGED,
        )
        
        doNothing().whenever(chargePointPaymentPGProcessor).cancel(any(), any())

        //when
        failChargePointPaymentListener.failHandler(FailChargePointPaymentEvent.create(order.orderId))

        //then
        val failChargePointPayment = chargePointPaymentRepository.findByOrderId(order.orderId)
        Assertions.assertThat(failChargePointPayment.chargePointPaymentStatus)
            .isEqualTo(ChargePointPaymentStatus.CANCELED)
    }
}