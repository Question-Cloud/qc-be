package com.eager.questioncloud.core.domain.point.implement;

import com.eager.questioncloud.core.domain.point.repository.ChargePointOrderRepository;
import com.eager.questioncloud.core.domain.point.model.ChargePointOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointOrderUpdater {
    private final ChargePointOrderRepository chargePointOrderRepository;

    public void save(ChargePointOrder chargePointOrder) {
        chargePointOrderRepository.save(chargePointOrder);
    }
}
