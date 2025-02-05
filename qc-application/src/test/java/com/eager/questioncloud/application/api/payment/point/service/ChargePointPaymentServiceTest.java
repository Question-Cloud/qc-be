package com.eager.questioncloud.application.api.payment.point.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import com.eager.questioncloud.application.business.payment.point.event.ChargePointPaymentEvent;
import com.eager.questioncloud.application.business.payment.point.implement.ChargePointPaymentPostProcessor;
import com.eager.questioncloud.application.business.payment.point.service.ChargePointPaymentService;
import com.eager.questioncloud.core.domain.point.enums.ChargePointPaymentStatus;
import com.eager.questioncloud.core.domain.point.enums.ChargePointType;
import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository;
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import com.eager.questioncloud.pg.dto.PGPayment;
import com.eager.questioncloud.pg.implement.PGPaymentProcessor;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

@SpringBootTest
@RecordApplicationEvents
@ActiveProfiles("test")
class ChargePointPaymentServiceTest {
    @Autowired
    private ChargePointPaymentService chargePointPaymentService;

    @Autowired
    private ChargePointPaymentRepository chargePointPaymentRepository;

    @MockBean
    private PGPaymentProcessor pgPaymentProcessor;

    @Autowired
    private ApplicationEvents events;

    @MockBean
    private ChargePointPaymentPostProcessor chargePointPaymentPostProcessor;

    @AfterEach
    void tearDown() {
        chargePointPaymentRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("포인트 충전 결제 주문을 생성할 수 있다.")
    void createOrder() {
        //given
        String paymentId = RandomStringUtils.randomAlphanumeric(10);
        Long userId = 1L;
        ChargePointPayment createOrder = ChargePointPayment.order(paymentId, userId, ChargePointType.PackageA);

        //when
        chargePointPaymentService.createOrder(createOrder);

        //then
        ChargePointPayment savedOrder = chargePointPaymentRepository.findByPaymentId(paymentId);
        assertThat(savedOrder.getPaymentId()).isEqualTo(paymentId);
        assertThat(savedOrder.getUserId()).isEqualTo(userId);
        assertThat(savedOrder.getChargePointType()).isEqualTo(ChargePointType.PackageA);
        assertThat(savedOrder.getChargePointPaymentStatus()).isEqualTo(ChargePointPaymentStatus.ORDERED);
    }

    @Test
    @DisplayName("동일한 주문 번호로 포인트 충전 주문을 생성할 수 없다.")
    void cannotCreateOrderSamePaymentId() {
        String paymentId1 = RandomStringUtils.randomAlphanumeric(10);
        Long userId = 1L;
        ChargePointPayment order1 = ChargePointPayment.order(paymentId1, userId, ChargePointType.PackageA);
        chargePointPaymentRepository.save(order1);

        ChargePointPayment order2 = ChargePointPayment.order(paymentId1, userId, ChargePointType.PackageA);

        //when then
        assertThatThrownBy(() -> chargePointPaymentService.createOrder(order2))
            .isInstanceOf(CoreException.class)
            .hasFieldOrPropertyWithValue("error", Error.ALREADY_ORDERED);
    }

    @Test
    @DisplayName("포인트 충전 결제를 승인할 수 있다.")
    void approvePayment() {
        //given
        String paymentId = RandomStringUtils.randomAlphanumeric(10);
        Long userId = 1L;
        ChargePointPayment order = ChargePointPayment.order(paymentId, userId, ChargePointType.PackageA);
        chargePointPaymentRepository.save(order);

        BDDMockito.given(pgPaymentProcessor.getPayment(any()))
            .willReturn(new PGPayment(order.getPaymentId(), ChargePointType.PackageA.getAmount(), "https://www.naver.com"));

        BDDMockito.willDoNothing().given(chargePointPaymentPostProcessor).chargeUserPoint(any());

        //then
        chargePointPaymentService.approvePayment(paymentId);

        //then
        ChargePointPayment paymentResult = chargePointPaymentRepository.findByPaymentId(paymentId);

        assertThat(paymentResult.getPaymentId()).isEqualTo(paymentId);
        assertThat(paymentResult.getChargePointPaymentStatus()).isEqualTo(ChargePointPaymentStatus.PAID);

        long eventCount = events.stream(ChargePointPaymentEvent.class).count();
        assertThat(eventCount).isEqualTo(1);
    }
}