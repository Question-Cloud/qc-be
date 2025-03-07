package com.eager.questioncloud.application.business.payment.point.implement

import com.eager.questioncloud.application.message.FailChargePointPaymentMessage
import com.eager.questioncloud.application.message.MessageSender
import com.eager.questioncloud.application.message.MessageType
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
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.springframework.amqp.rabbit.test.RabbitListenerTest
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import java.util.*

@SpringBootTest
@RabbitListenerTest
@ActiveProfiles("test")
internal class FailChargePointPaymentMessageListenerTest {
    @Autowired
    private val chargePointPaymentRepository: ChargePointPaymentRepository? = null

    @Autowired
    private val messageSender: MessageSender? = null

    @Autowired
    private val harness: RabbitListenerTestHarness? = null

    @MockBean
    private val pgPaymentProcessor: PGPaymentProcessor? = null

    @AfterEach
    fun tearDown() {
        chargePointPaymentRepository!!.deleteAllInBatch()
    }

    @Test
    @DisplayName("FailChargePointPaymentMessage를 처리할 수 있다.")
    @Throws(
        Exception::class
    )
    fun failHandler() {
        //given
        val paymentId = UUID.randomUUID().toString()
        chargePointPaymentRepository!!.save(order(paymentId, 1L, ChargePointType.PackageA))

        val listener = harness!!.getSpy<FailChargePointPaymentMessageListener>("fail.charge.point")
        val answer = harness.getLatchAnswerFor("fail.charge.point", 1)
        Mockito.doAnswer(answer).`when`(listener).failHandler(any())

        BDDMockito.willDoNothing().given(pgPaymentProcessor)!!.cancel(any())

        //when
        messageSender!!.sendMessage(MessageType.FAIL_CHARGE_POINT, FailChargePointPaymentMessage.create(paymentId))
        answer.await(10)

        //then
        val failChargePointPayment = chargePointPaymentRepository.findByPaymentId(paymentId)
        Assertions.assertThat(failChargePointPayment.chargePointPaymentStatus).isEqualTo(ChargePointPaymentStatus.Fail)
    }
}