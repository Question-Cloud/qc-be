package com.eager.questioncloud.application.api.payment.point.implement;

import com.eager.questioncloud.application.api.payment.point.event.FailChargePointPaymentEvent;
import com.eager.questioncloud.application.message.MessageSender;
import com.eager.questioncloud.application.message.MessageType;
import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository;
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import com.eager.questioncloud.lock.LockKeyGenerator;
import com.eager.questioncloud.lock.LockManager;
import com.eager.questioncloud.pg.dto.PGPayment;
import com.eager.questioncloud.pg.exception.InvalidPaymentIdException;
import com.eager.questioncloud.pg.implement.PGPaymentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointPaymentApprover {
    private final ChargePointPaymentRepository chargePointPaymentRepository;
    private final PGPaymentProcessor pgPaymentProcessor;
    private final LockManager lockManager;
    private final MessageSender messageSender;

    public ChargePointPayment approve(String paymentId) {
        try {
            PGPayment pgPayment = pgPaymentProcessor.getPayment(paymentId);
            return lockManager.executeWithLock(
                LockKeyGenerator.generateChargePointPaymentKey(pgPayment.getPaymentId()),
                () -> {
                    ChargePointPayment chargePointPayment = chargePointPaymentRepository.findByPaymentId(pgPayment.getPaymentId());
                    chargePointPayment.validatePayment(pgPayment.getAmount());
                    chargePointPayment.approve(pgPayment.getReceiptUrl());
                    return chargePointPaymentRepository.save(chargePointPayment);
                }
            );
        } catch (InvalidPaymentIdException e) {
            throw new CoreException(Error.NOT_FOUND);
        } catch (CoreException coreException) {
            throw coreException;
        } catch (Exception unknownException) {
            messageSender.sendMessage(MessageType.FAIL_CHARGE_POINT, new FailChargePointPaymentEvent(paymentId));
            throw unknownException;
        }
    }
}
