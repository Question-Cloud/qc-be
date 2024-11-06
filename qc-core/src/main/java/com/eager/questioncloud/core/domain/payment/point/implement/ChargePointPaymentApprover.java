package com.eager.questioncloud.core.domain.payment.point.implement;

import com.eager.questioncloud.core.domain.payment.point.model.ChargePointPayment;
import com.eager.questioncloud.core.domain.payment.point.repository.ChargePointPaymentRepository;
import com.eager.questioncloud.core.domain.pg.PGAPI;
import com.eager.questioncloud.core.domain.pg.PGPayment;
import com.eager.questioncloud.core.exception.CustomException;
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
            PGPayment pgPayment = pgAPI.getPayment(paymentId);
            ChargePointPayment chargePointPayment = chargePointPaymentRepository.getChargePointPaymentForApprove(paymentId);
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
