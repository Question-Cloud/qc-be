package com.eager.questioncloud.core.domain.point.implement;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment;
import com.eager.questioncloud.core.domain.point.repository.ChargePointPaymentRepository;
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
