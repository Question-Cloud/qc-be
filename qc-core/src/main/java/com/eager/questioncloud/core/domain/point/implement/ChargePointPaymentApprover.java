package com.eager.questioncloud.core.domain.point.implement;

import com.eager.questioncloud.core.domain.pg.PGAPI;
import com.eager.questioncloud.core.domain.pg.PGPayment;
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment;
import com.eager.questioncloud.core.domain.point.repository.ChargePointPaymentRepository;
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
