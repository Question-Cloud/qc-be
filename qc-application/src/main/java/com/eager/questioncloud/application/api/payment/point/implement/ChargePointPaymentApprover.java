package com.eager.questioncloud.application.api.payment.point.implement;

import com.eager.questioncloud.core.domain.point.dto.PGPayment;
import com.eager.questioncloud.core.domain.point.infrastructure.ChargePointPaymentRepository;
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment;
import com.eager.questioncloud.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointPaymentApprover {
    private final ChargePointPaymentRepository chargePointPaymentRepository;
    private final ChargePointPaymentFailHandler chargePointPaymentFailHandler;
    private final PGAPI pgAPI;

    public ChargePointPayment approve(String paymentId) {
        try {
            ChargePointPayment chargePointPayment = chargePointPaymentRepository.getChargePointPaymentForApprove(paymentId);
            PGPayment pgPayment = pgAPI.getPayment(paymentId);
            chargePointPayment.approve(pgPayment);
            return chargePointPaymentRepository.save(chargePointPayment);
        } catch (CustomException customException) {
            throw customException;
        } catch (Exception unknownException) {
            chargePointPaymentFailHandler.failHandler(paymentId);
            throw unknownException;
        }
    }
}
