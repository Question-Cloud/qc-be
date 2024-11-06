package com.eager.questioncloud.core.domain.payment.point.implement;

import com.eager.questioncloud.core.domain.payment.point.model.ChargePointPayment;
import com.eager.questioncloud.core.domain.payment.point.repository.ChargePointPaymentRepository;
import com.eager.questioncloud.core.domain.pg.PGPayment;
import com.eager.questioncloud.core.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointPaymentApprover {
    private final ChargePointPaymentRepository chargePointPaymentRepository;
    private final ChargePointPaymentFailHandler chargePointPaymentFailHandler;

    public void approve(ChargePointPayment chargePointPayment, PGPayment pgPayment) {
        try {
            chargePointPayment.approve(pgPayment);
            chargePointPaymentRepository.save(chargePointPayment);
        } catch (CustomException customException) {
            throw customException;
        } catch (Exception unknownException) {
            chargePointPaymentFailHandler.failHandler(chargePointPayment.getPaymentId());
            throw unknownException;
        }
    }
}
