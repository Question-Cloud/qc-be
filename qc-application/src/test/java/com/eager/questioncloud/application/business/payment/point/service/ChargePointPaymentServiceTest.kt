package com.eager.questioncloud.application.business.payment.point.service

import com.eager.questioncloud.application.business.payment.point.event.ChargePointPaymentEvent
import com.eager.questioncloud.application.business.payment.point.implement.ChargePointPaymentPostProcessor
import com.eager.questioncloud.core.domain.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.core.domain.point.enums.ChargePointType
import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment.Companion.order
import com.eager.questioncloud.pg.dto.PGPayment
import com.eager.questioncloud.pg.implement.PGPaymentProcessor
import org.apache.commons.lang3.RandomStringUtils
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
import org.springframework.test.context.event.ApplicationEvents
import org.springframework.test.context.event.RecordApplicationEvents

@SpringBootTest
@RecordApplicationEvents
@ActiveProfiles("test")
internal class ChargePointPaymentServiceTest {
    @Autowired
    private val chargePointPaymentService: ChargePointPaymentService? = null

    @Autowired
    private val chargePointPaymentRepository: ChargePointPaymentRepository? = null

    @MockBean
    private val pgPaymentProcessor: PGPaymentProcessor? = null

    @Autowired
    private val events: ApplicationEvents? = null

    @MockBean
    private val chargePointPaymentPostProcessor: ChargePointPaymentPostProcessor? = null

    @AfterEach
    fun tearDown() {
        chargePointPaymentRepository!!.deleteAllInBatch()
    }

    @Test
    @DisplayName("포인트 충전 결제 주문을 생성할 수 있다.")
    fun createOrder() {
        //given
        val paymentId = RandomStringUtils.randomAlphanumeric(10)
        val userId = 1L
        val createOrder = order(paymentId, userId, ChargePointType.PackageA)

        //when
        chargePointPaymentService!!.createOrder(createOrder)

        //then
        val savedOrder = chargePointPaymentRepository!!.findByPaymentId(paymentId)
        Assertions.assertThat(savedOrder.paymentId).isEqualTo(paymentId)
        Assertions.assertThat(savedOrder.userId).isEqualTo(userId)
        Assertions.assertThat(savedOrder.chargePointType).isEqualTo(ChargePointType.PackageA)
        Assertions.assertThat(savedOrder.chargePointPaymentStatus).isEqualTo(ChargePointPaymentStatus.ORDERED)
    }

    @Test
    @DisplayName("포인트 충전 결제를 승인할 수 있다.")
    fun approvePayment() {
        //given
        val paymentId = RandomStringUtils.randomAlphanumeric(10)
        val userId = 1L
        val order = order(paymentId, userId, ChargePointType.PackageA)
        chargePointPaymentRepository!!.save(order)

        BDDMockito.given(pgPaymentProcessor!!.getPayment(any()))
            .willReturn(PGPayment(order.paymentId, ChargePointType.PackageA.amount, "https://www.naver.com"))

        BDDMockito.willDoNothing().given(chargePointPaymentPostProcessor)!!.chargeUserPoint(any())

        //then
        chargePointPaymentService!!.approvePayment(paymentId)

        //then
        val paymentResult = chargePointPaymentRepository.findByPaymentId(paymentId)

        Assertions.assertThat(paymentResult.paymentId).isEqualTo(paymentId)
        Assertions.assertThat(paymentResult.chargePointPaymentStatus).isEqualTo(ChargePointPaymentStatus.PAID)

        val eventCount = events!!.stream(ChargePointPaymentEvent::class.java).count()
        Assertions.assertThat(eventCount).isEqualTo(1)
    }
}