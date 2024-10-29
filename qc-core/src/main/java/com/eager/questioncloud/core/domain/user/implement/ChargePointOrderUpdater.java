package com.eager.questioncloud.core.domain.user.implement;

import com.eager.questioncloud.core.domain.user.model.ChargePointOrder;
import com.eager.questioncloud.core.domain.user.repository.ChargePointOrderRepository;
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
