package com.eager.questioncloud.core.domain.payment.point.implement;

import com.eager.questioncloud.core.domain.payment.point.model.ChargePointPayment;
import com.eager.questioncloud.core.domain.portone.dto.PortonePayment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ChargePointPaymentProcessor {
    private final ChargePointPaymentAppender chargePointPaymentAppender;
    private final ChargePointPaymentReader chargePointPaymentReader;
    private final ChargePointPaymentUpdater chargePointPaymentUpdater;

    @Transactional
    public ChargePointPayment payment(PortonePayment portonePayment) {
        ChargePointPayment chargePointPayment = chargePointPaymentReader.getChargePointPayment(portonePayment.getId());
        chargePointPayment.paid(portonePayment);
        chargePointPaymentUpdater.save(chargePointPayment);
        return chargePointPayment;
    }

    public ChargePointPayment createOrder(ChargePointPayment chargePointPayment) {
        return chargePointPaymentAppender.append(chargePointPayment);
    }

    public Boolean isCompletePayment(Long userId, String paymentId) {
        return chargePointPaymentReader.isCompletePayment(userId, paymentId);
    }
}
