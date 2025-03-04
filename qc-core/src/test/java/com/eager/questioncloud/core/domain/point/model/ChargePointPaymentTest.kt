package com.eager.questioncloud.core.domain.point.model;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.eager.questioncloud.core.domain.point.enums.ChargePointType;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import com.eager.questioncloud.core.exception.InvalidPaymentException;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChargePointPaymentTest {

    @Test
    @DisplayName("포인트 충전 결제 검증을 할 수 있다.")
    void validatePayment() {
        //given
        String paymentId = UUID.randomUUID().toString();
        Long userId = 1L;
        ChargePointType chargePointType = ChargePointType.PackageC;
        ChargePointPayment chargePointPayment = ChargePointPayment.order(paymentId, userId, chargePointType);
        int paidAmount = chargePointType.getAmount();

        // when then
        assertThatNoException().isThrownBy(() -> chargePointPayment.validatePayment(paidAmount));
    }

    @Test
    @DisplayName("결제 금액이 올바르지 않을 경우 결제 검증에 실패한다.")
    void failValidatePaymentWhenInCorrectAmount() {
        //given
        String paymentId = UUID.randomUUID().toString();
        Long userId = 1L;
        ChargePointType chargePointType = ChargePointType.PackageC;
        ChargePointPayment chargePointPayment = ChargePointPayment.order(paymentId, userId, chargePointType);
        int wrongAmount = 100;

        //when then
        assertThatThrownBy(() -> chargePointPayment.validatePayment(wrongAmount))
            .isInstanceOf(InvalidPaymentException.class);
    }

    @Test
    @DisplayName("이미 처리 된 결제인 경우 검증에 실패한다.")
    void failValidatePaymentWhenAlreadyApproved() {
        //given
        String paymentId1 = UUID.randomUUID().toString();
        Long userId = 1L;
        ChargePointType chargePointType = ChargePointType.PackageC;
        ChargePointPayment chargePointPayment1 = ChargePointPayment.order(paymentId1, userId, chargePointType);
        chargePointPayment1.approve("approved");

        String paymentId2 = UUID.randomUUID().toString();
        ChargePointPayment chargePointPayment2 = ChargePointPayment.order(paymentId2, userId, chargePointType);
        chargePointPayment2.fail();

        int paidAmount = chargePointType.getAmount();

        //when then
        assertThatThrownBy(() -> chargePointPayment1.validatePayment(paidAmount))
            .isInstanceOf(CoreException.class)
            .hasFieldOrPropertyWithValue("error", Error.ALREADY_PROCESSED_PAYMENT);

        assertThatThrownBy(() -> chargePointPayment2.validatePayment(paidAmount))
            .isInstanceOf(CoreException.class)
            .hasFieldOrPropertyWithValue("error", Error.ALREADY_PROCESSED_PAYMENT);
    }
}