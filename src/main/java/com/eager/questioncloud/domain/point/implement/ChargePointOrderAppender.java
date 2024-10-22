package com.eager.questioncloud.domain.point.implement;

import com.eager.questioncloud.domain.point.model.ChargePointOrder;
import com.eager.questioncloud.domain.point.repository.ChargePointOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointOrderAppender {
    private final ChargePointOrderRepository chargePointOrderRepository;

    public ChargePointOrder append(ChargePointOrder chargePointOrder) {
        return chargePointOrderRepository.append(chargePointOrder);
    }
}
