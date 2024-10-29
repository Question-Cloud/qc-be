package com.eager.questioncloud.core.domain.user.point.implement;

import com.eager.questioncloud.core.domain.portone.dto.PortonePayment;
import com.eager.questioncloud.core.domain.user.dto.ChargePointPaymentResult;
import com.eager.questioncloud.core.domain.user.point.model.ChargePointPayment;
import com.eager.questioncloud.core.domain.user.point.model.ChargePointOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ChargePointPaymentProcessor {
    private final ChargePointOrderReader chargePointOrderReader;
    private final ChargePointOrderUpdater chargePointOrderUpdater;
    private final ChargePointPaymentAppender chargePointPaymentAppender;

    @Transactional
    public ChargePointPaymentResult payment(PortonePayment portonePayment) {
        ChargePointOrder chargePointOrder = chargePointOrderReader.findByPaymentId(portonePayment.getId());
        chargePointOrder.paid(portonePayment);
        chargePointOrderUpdater.save(chargePointOrder);

        ChargePointPayment chargePointPayment = chargePointPaymentAppender.append(
            ChargePointPayment.create(
                chargePointOrder.getUserId(),
                portonePayment));

        return new ChargePointPaymentResult(chargePointOrder, chargePointPayment);
    }
}
