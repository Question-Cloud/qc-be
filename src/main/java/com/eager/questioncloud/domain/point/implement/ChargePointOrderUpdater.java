package com.eager.questioncloud.domain.point.implement;

import com.eager.questioncloud.domain.point.model.ChargePointOrder;
import com.eager.questioncloud.domain.point.repository.ChargePointOrderRepository;
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
