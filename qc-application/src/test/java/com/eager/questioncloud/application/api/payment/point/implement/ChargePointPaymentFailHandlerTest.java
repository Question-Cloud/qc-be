package com.eager.questioncloud.application.api.payment.point.implement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

import com.eager.questioncloud.application.message.FailChargePointPaymentMessage;
import com.eager.questioncloud.application.message.MessageSender;
import com.eager.questioncloud.application.message.MessageType;
import com.eager.questioncloud.core.domain.point.enums.ChargePointPaymentStatus;
import com.eager.questioncloud.core.domain.point.enums.ChargePointType;
import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository;
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment;
import com.eager.questioncloud.pg.implement.PGPaymentProcessor;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.amqp.rabbit.test.RabbitListenerTest;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.mockito.LatchCountDownAndCallRealMethodAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@RabbitListenerTest
@ActiveProfiles("test")
class ChargePointPaymentFailHandlerTest {
    @Autowired
    private ChargePointPaymentRepository chargePointPaymentRepository;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private RabbitListenerTestHarness harness;

    @MockBean
    private PGPaymentProcessor pgPaymentProcessor;

    @AfterEach
    void tearDown() {
        chargePointPaymentRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("FailChargePointPaymentMessage를 처리할 수 있다.")
    void failHandler() throws Exception {
        //given
        String paymentId = UUID.randomUUID().toString();
        chargePointPaymentRepository.save(ChargePointPayment.order(paymentId, 1L, ChargePointType.PackageA));

        ChargePointPaymentFailHandler listener = harness.getSpy("fail-charge-point");
        LatchCountDownAndCallRealMethodAnswer answer = harness.getLatchAnswerFor("fail-charge-point", 1);
        doAnswer(answer).when(listener).failHandler(any());

        BDDMockito.willDoNothing().given(pgPaymentProcessor).cancel(any());

        //when
        messageSender.sendMessage(MessageType.FAIL_CHARGE_POINT, new FailChargePointPaymentMessage(paymentId));
        answer.await(10);

        //then
        ChargePointPayment failChargePointPayment = chargePointPaymentRepository.findByPaymentId(paymentId);
        assertThat(failChargePointPayment.getChargePointPaymentStatus()).isEqualTo(ChargePointPaymentStatus.Fail);
    }
}