package com.eager.questioncloud.point.implement;

import com.eager.questioncloud.point.dto.ChargePointDto.ChargePointPaymentResult;
import com.eager.questioncloud.point.model.ChargePointOrder;
import com.eager.questioncloud.point.model.ChargePointPayment;
import com.eager.questioncloud.portone.dto.PortonePayment;
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
        ChargePointOrder chargePointOrder = chargePointOrderReader.get(portonePayment.getId());
        chargePointOrder.paid(portonePayment);
        chargePointOrderUpdater.save(chargePointOrder);

        ChargePointPayment chargePointPayment = chargePointPaymentAppender.append(
            ChargePointPayment.create(
                chargePointOrder.getUserId(),
                portonePayment));

        return new ChargePointPaymentResult(chargePointOrder, chargePointPayment);
    }
}
