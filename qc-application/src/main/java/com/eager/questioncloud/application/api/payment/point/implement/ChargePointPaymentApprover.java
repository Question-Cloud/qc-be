package com.eager.questioncloud.application.api.payment.point.implement;

import com.eager.questioncloud.application.api.payment.point.event.FailChargePointPaymentEvent;
import com.eager.questioncloud.application.message.MessageSender;
import com.eager.questioncloud.application.message.MessageType;
import com.eager.questioncloud.core.domain.point.dto.PGPayment;
import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository;
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import com.eager.questioncloud.core.exception.InvalidPaymentException;
import com.eager.questioncloud.lock.LockKeyGenerator;
import com.eager.questioncloud.lock.LockManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointPaymentApprover {
    private final ChargePointPaymentRepository chargePointPaymentRepository;
    private final PGPaymentProcessor pgPaymentProcessor;
    private final MessageSender messageSender;
    private final LockManager lockManager;

    public ChargePointPayment approve(String paymentId) {
        PGPayment pgPayment = getChargePointPGPayment(paymentId);
        return lockManager.executeWithLock(
            LockKeyGenerator.generateChargePointPaymentKey(pgPayment.getPaymentId()),
            () -> {
                try {
                    ChargePointPayment chargePointPayment = chargePointPaymentRepository.findByPaymentId(pgPayment.getPaymentId());
                    chargePointPayment.validatePayment(pgPayment.getAmount());
                    chargePointPayment.approve(pgPayment.getReceiptUrl());
                    return chargePointPaymentRepository.save(chargePointPayment);
                } catch (CoreException coreException) {
                    throw coreException;
                } catch (Exception unknownException) {
                    messageSender.sendMessage(MessageType.FAIL_CHARGE_POINT, new FailChargePointPaymentEvent(pgPayment.getPaymentId()));
                    throw unknownException;
                }
            }
        );
    }

    private PGPayment getChargePointPGPayment(String paymentId) {
        try {
            return pgPaymentProcessor.getPayment(paymentId);
        } catch (InvalidPaymentException e) {
            throw new CoreException(Error.NOT_FOUND);
        } catch (Exception unknownException) {
            messageSender.sendMessage(MessageType.FAIL_CHARGE_POINT, new FailChargePointPaymentEvent(paymentId));
            throw unknownException;
        }
    }
}
