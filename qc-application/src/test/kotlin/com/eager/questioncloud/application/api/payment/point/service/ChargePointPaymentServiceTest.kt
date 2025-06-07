package com.eager.questioncloud.application.api.payment.point.service

import com.eager.questioncloud.application.api.payment.point.implement.ChargePointPaymentPGProcessor
import com.eager.questioncloud.application.utils.DBCleaner
import com.eager.questioncloud.core.domain.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.core.domain.point.enums.ChargePointType
import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment
import com.eager.questioncloud.pg.dto.PGPayment
import com.eager.questioncloud.pg.toss.PaymentStatus
import org.apache.commons.lang3.RandomStringUtils
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.given
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.event.RecordApplicationEvents

@SpringBootTest
@RecordApplicationEvents
@ActiveProfiles("test")
internal class ChargePointPaymentServiceTest(
    @Autowired val chargePointPaymentService: ChargePointPaymentService,
    @Autowired val chargePointPaymentRepository: ChargePointPaymentRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @MockBean
    lateinit var chargePointPaymentPGProcessor: ChargePointPaymentPGProcessor

    @AfterEach
    fun tearDown() {
        chargePointPaymentRepository.deleteAllInBatch()
        dbCleaner.cleanUp()
    }

    @Test
    fun `포인트 충전 주문을 생성할 수 있다`() {
        //given
        val userId = 1L
        val chargePointType = ChargePointType.PackageA

        //when
        val orderId = chargePointPaymentService.createOrder(userId, chargePointType)

        //then
        val savedOrder = chargePointPaymentRepository.findByOrderId(orderId)
        Assertions.assertThat(savedOrder.orderId).isEqualTo(orderId)
        Assertions.assertThat(savedOrder.userId).isEqualTo(userId)
        Assertions.assertThat(savedOrder.chargePointType).isEqualTo(ChargePointType.PackageA)
        Assertions.assertThat(savedOrder.chargePointPaymentStatus).isEqualTo(ChargePointPaymentStatus.ORDERED)
    }

    @Test
    fun `포인트 충전 결제를 승인할 수 있다`() {
        //given
        val paymentId = RandomStringUtils.randomAlphanumeric(10)

        val userId = 1L

        val order = ChargePointPayment.createOrder(userId, ChargePointType.PackageA)
        chargePointPaymentRepository.save(order)

        val pgPayment = PGPayment(paymentId, order.orderId, ChargePointType.PackageA.amount, PaymentStatus.DONE)

        given(chargePointPaymentPGProcessor.getPayment(any())).willReturn(pgPayment)
        doNothing().whenever(chargePointPaymentPGProcessor).confirm(any(), any(), any())

        //then
        chargePointPaymentService.approvePayment(order.orderId)

        //then
        val paymentResult = chargePointPaymentRepository.findByOrderId(order.orderId)

        Assertions.assertThat(paymentResult.paymentId).isEqualTo(paymentId)
        Assertions.assertThat(paymentResult.chargePointPaymentStatus).isEqualTo(ChargePointPaymentStatus.CHARGED)
    }
}