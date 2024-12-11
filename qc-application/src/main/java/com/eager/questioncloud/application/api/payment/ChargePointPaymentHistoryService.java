package com.eager.questioncloud.application.api.payment;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.point.infrastructure.ChargePointPaymentRepository;
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChargePointPaymentHistoryService {
    private final ChargePointPaymentRepository chargePointPaymentRepository;

    public List<ChargePointPayment> getChargePointPayments(Long userId, PagingInformation pagingInformation) {
        return chargePointPaymentRepository.getChargePointPayments(userId, pagingInformation);
    }

    public int countChargePointPayment(Long userId) {
        return chargePointPaymentRepository.countByUserId(userId);
    }
}
