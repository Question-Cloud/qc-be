package com.eager.questioncloud.application.api.payment.point.implement;

import com.eager.questioncloud.core.domain.point.dto.PGPayment;
import com.eager.questioncloud.core.domain.point.infrastructure.ChargePointPaymentRepository;
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.lock.LockKeyGenerator;
import com.eager.questioncloud.lock.LockManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointPaymentApprover {
    private final ChargePointPaymentRepository chargePointPaymentRepository;
    private final ChargePointPaymentFailHandler chargePointPaymentFailHandler;
    private final LockManager lockManager;

    public ChargePointPayment approve(PGPayment pgPayment) {
        return lockManager.executeWithLock(
            LockKeyGenerator.generateChargePointPaymentKey(pgPayment.getPaymentId()),
            () -> {
                try {
                    ChargePointPayment chargePointPayment = chargePointPaymentRepository.getChargePointPaymentForApprove(pgPayment.getPaymentId());
                    chargePointPayment.approve(pgPayment);
                    return chargePointPaymentRepository.save(chargePointPayment);
                } catch (CustomException customException) {
                    throw customException;
                } catch (Exception unknownException) {
                    chargePointPaymentFailHandler.failHandler(pgPayment.getPaymentId());
                    throw unknownException;
                }
            }
        );
    }
}