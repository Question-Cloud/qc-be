package com.eager.questioncloud.application.business.payment.point.implement;

import com.eager.questioncloud.application.message.FailChargePointPaymentMessage;
import com.eager.questioncloud.application.message.MessageSender;
import com.eager.questioncloud.application.message.MessageType;
import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository;
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.pg.dto.PGPayment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ChargePointPaymentApprover {
    private final ChargePointPaymentRepository chargePointPaymentRepository;
    private final MessageSender messageSender;

    @Transactional
    public ChargePointPayment approve(PGPayment pgPayment) {
        try {
            ChargePointPayment chargePointPayment = chargePointPaymentRepository.findByPaymentIdWithLock(pgPayment.getPaymentId());
            chargePointPayment.validatePayment(pgPayment.getAmount());
            chargePointPayment.approve(pgPayment.getReceiptUrl());
            return chargePointPaymentRepository.save(chargePointPayment);
        } catch (CoreException coreException) {
            throw coreException;
        } catch (Exception unknownException) {
            messageSender.sendMessage(MessageType.FAIL_CHARGE_POINT, FailChargePointPaymentMessage.create(pgPayment.getPaymentId()));
            throw unknownException;
        }
    }
}
