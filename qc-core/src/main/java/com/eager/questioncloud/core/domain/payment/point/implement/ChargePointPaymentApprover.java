package com.eager.questioncloud.core.domain.payment.point.implement;

import com.eager.questioncloud.core.domain.payment.point.model.ChargePointPayment;
import com.eager.questioncloud.core.domain.payment.point.repository.ChargePointPaymentRepository;
import com.eager.questioncloud.core.domain.portone.dto.PortonePayment;
import com.eager.questioncloud.core.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointPaymentApprover {
    private final ChargePointPaymentRepository chargePointPaymentRepository;
    private final ChargePointPaymentExceptionHandler chargePointPaymentExceptionHandler;

    public void approve(ChargePointPayment chargePointPayment, PortonePayment portonePayment) {
        try {
            chargePointPayment.approve(portonePayment);
            chargePointPaymentRepository.save(chargePointPayment);
        } catch (Exception e) {
            if (e instanceof CustomException) {
                throw e;
            }
            chargePointPaymentExceptionHandler.failHandler(chargePointPayment);
            throw e;
        }
    }
}
