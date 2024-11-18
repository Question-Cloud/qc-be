package com.eager.questioncloud.application.payment;


import com.eager.questioncloud.domain.point.PGPayment;
import com.eager.questioncloud.pg.portone.PortoneAPI;
import com.eager.questioncloud.pg.portone.PortonePayment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PGAPI {
    private final PortoneAPI portoneAPI;

    public PGPayment getPayment(String paymentId) {
        PortonePayment portonePayment = portoneAPI.getPayment(paymentId);
        return new PGPayment(portonePayment.getId(), portonePayment.getAmount().getTotal(), portonePayment.getReceiptUrl());
    }

    public void cancel(String paymentId) {
        portoneAPI.cancel(paymentId);
    }
}
