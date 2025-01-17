package com.eager.questioncloud.application.api.payment.point.implement;

import com.eager.questioncloud.application.message.FailChargePointPaymentMessage;
import com.eager.questioncloud.application.message.MessageSender;
import com.eager.questioncloud.application.message.MessageType;
import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository;
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import com.eager.questioncloud.pg.dto.PGPayment;
import com.eager.questioncloud.pg.exception.InvalidPaymentIdException;
import com.eager.questioncloud.pg.implement.PGPaymentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Component
@RequiredArgsConstructor
public class ChargePointPaymentApprover {
    private final ChargePointPaymentRepository chargePointPaymentRepository;
    private final PGPaymentProcessor pgPaymentProcessor;
    private final TransactionTemplate transactionTemplate;
    private final MessageSender messageSender;

    public ChargePointPayment approve(String paymentId) {
        try {
            PGPayment pgPayment = pgPaymentProcessor.getPayment(paymentId);
            return transactionTemplate.execute((status) -> {
                ChargePointPayment chargePointPayment = chargePointPaymentRepository.findByPaymentIdWithLock(pgPayment.getPaymentId());
                chargePointPayment.validatePayment(pgPayment.getAmount());
                chargePointPayment.approve(pgPayment.getReceiptUrl());
                return chargePointPaymentRepository.save(chargePointPayment);
            });
        } catch (InvalidPaymentIdException e) {
            throw new CoreException(Error.NOT_FOUND);
        } catch (CoreException coreException) {
            throw coreException;
        } catch (Exception unknownException) {
            messageSender.sendMessage(MessageType.FAIL_CHARGE_POINT, new FailChargePointPaymentMessage(paymentId));
            throw unknownException;
        }
    }
}
