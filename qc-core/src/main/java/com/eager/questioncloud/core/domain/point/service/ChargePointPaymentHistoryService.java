package com.eager.questioncloud.core.domain.point.service;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.point.implement.ChargePointPaymentReader;
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChargePointPaymentHistoryService {
    private final ChargePointPaymentReader chargePointPaymentReader;

    public List<ChargePointPayment> getChargePointPayments(Long userId, PagingInformation pagingInformation) {
        return chargePointPaymentReader.getChargePointPayments(userId, pagingInformation);
    }

    public int countChargePointPayment(Long userId) {
        return chargePointPaymentReader.countChargePointPayment(userId);
    }
}
