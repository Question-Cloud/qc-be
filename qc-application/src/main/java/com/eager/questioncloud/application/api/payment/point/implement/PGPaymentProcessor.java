package com.eager.questioncloud.application.api.payment.point.implement;


import com.eager.questioncloud.application.message.MessageSender;
import com.eager.questioncloud.application.message.MessageType;
import com.eager.questioncloud.core.domain.point.dto.PGPayment;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import com.eager.questioncloud.pg.exception.PGException;
import com.eager.questioncloud.pg.portone.PortoneAPI;
import com.eager.questioncloud.pg.portone.PortonePayment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PGPaymentProcessor {
    private final PortoneAPI portoneAPI;
    private final MessageSender messageSender;

    public PGPayment getPayment(String paymentId) {
        try {
            PortonePayment portonePayment = portoneAPI.getPayment(paymentId);
            return new PGPayment(portonePayment.getId(), portonePayment.getAmount().getTotal(), portonePayment.getReceiptUrl());
        } catch (PGException e) {
            messageSender.sendMessage(MessageType.FAIL_CHARGE_POINT, paymentId);
            throw new CoreException(Error.PAYMENT_ERROR);
        }
    }

    public void cancel(String paymentId) {
        portoneAPI.cancel(paymentId);
    }
}
