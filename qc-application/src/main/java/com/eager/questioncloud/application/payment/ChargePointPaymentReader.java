package com.eager.questioncloud.application.payment;

import com.eager.questioncloud.common.PagingInformation;
import com.eager.questioncloud.domain.point.ChargePointPayment;
import com.eager.questioncloud.domain.point.ChargePointPaymentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointPaymentReader {
    private final ChargePointPaymentRepository chargePointPaymentRepository;

    public Boolean isCompletedPayment(Long userId, String paymentId) {
        return chargePointPaymentRepository.isCompletedPayment(userId, paymentId);
    }

    public List<ChargePointPayment> getChargePointPayments(Long userId, PagingInformation pagingInformation) {
        return chargePointPaymentRepository.getChargePointPayments(userId, pagingInformation);
    }

    public int countChargePointPayment(Long userId) {
        return chargePointPaymentRepository.countByUserId(userId);
    }
}
